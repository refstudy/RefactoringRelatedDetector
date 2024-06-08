package org.metricsminer.metric.collector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.metricsminer.model.astnodes.FileAST;
import org.metricsminer.model.astnodes.TreeASTNode;
import org.metricsminer.model.diff.FileDiff;
import org.metricsminer.model.diff.NodeDiffPair;
import org.metricsminer.model.diff.NodeDiffPair.DiffType;

import com.github.gumtreediff.actions.ChawatheScriptGenerator;
import com.github.gumtreediff.actions.EditScript;
import com.github.gumtreediff.actions.EditScriptGenerator;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.actions.model.TreeDelete;
import com.github.gumtreediff.actions.model.TreeInsert;
import com.github.gumtreediff.actions.model.Update;
import com.github.gumtreediff.matchers.Mapping;
import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.Tree;

public abstract class GumTreeDiffCollector extends MetricCollector {

    //Necessário ser syncrhonized porque nessa versão do gumtree ele não suporta parallel
    @Override
    public synchronized void collect(FileDiff fileDiff) {

        ArrayList<NodeDiffPair> diffPairs = new ArrayList<>();
        try {

            FileAST before = fileDiff.getBeforeFileAst();
            FileAST after = fileDiff.getAfterFileAst();

            if (before == null && after == null) {
                System.out.println("WARNING: Before and After file is empty");
                return;
            }

            // Gumtree
            GumTreeJDTGenerator jdtTreeGenerator = new GumTreeJDTGenerator();
            Tree src = jdtTreeGenerator.generateFromFileAst(before).getRoot();
            Tree dst = jdtTreeGenerator.generateFromFileAst(after).getRoot();
            Matcher defaultMatcher = Matchers.getInstance().getMatcher("gumtree-hybrid-id");
            MappingStore mappings = defaultMatcher.match(src, dst);
            EditScriptGenerator editScriptGenerator2 = new ChawatheScriptGenerator();
            EditScript actions = editScriptGenerator2.computeActions(mappings); //

            HashMap<String, TreeASTNode> nodePerLineBefore = computeNodePosition(before, new HashMap<>());
            HashMap<String, TreeASTNode> nodePerLineAfter = computeNodePosition(after, new HashMap<>());

            // Mapear antes e depois para os casos de par
            for (Mapping map : mappings.asSet()) {
            
                if (!isValidNode(map.first)) {
                    continue;
                }

                TreeASTNode javaASTNodeBefore = nodePerLineBefore
                        .get(map.first.getType().toString() + map.first.getPos());
                TreeASTNode javaASTNodeAfter = nodePerLineAfter
                        .get(map.second.getType().toString() + map.second.getPos());

                if (javaASTNodeBefore == null || javaASTNodeAfter == null) {
                    System.out.println(map.first.getType().toString());
                    System.out.println("    line:" + before.getNode().getLineNumber(map.first.getPos()));
                    System.out.println("    column:" + before.getNode().getColumnNumber(map.first.getPos()));
                    System.out.println("    label:" + map.first.getLabel());
                    System.out.println(map.second.getType().toString());
                    System.out.println("    line:" + after.getNode().getLineNumber(map.second.getPos()));
                    System.out.println("    column:" + after.getNode().getColumnNumber(map.second.getPos()));
                    System.out.println("    label:" + map.second.getLabel());
                    throw new Exception("Detection of gumtree not found on JDT Ast");
                }

                diffPairs.add(new NodeDiffPair(javaASTNodeBefore, javaASTNodeAfter, DiffType.PAIR));

            }

            ArrayList<Action> insertAndRemoveActions = new ArrayList<>();

            boolean ignoreUpdate = true;
            // Mapear os casos de move, update
            // deixa insert e delete por ultimo para garantir que todos os pares detectados
            // ja foram atualizados anteriormente
            for (Action action : actions.asList()) {
                if (!isValidNode(action.getNode())) {
                    continue;
                }
                try {
                    String index = action.getNode().getType().toString() + action.getNode().getPos();
                    if (action instanceof Update && !ignoreUpdate) {
                        diffPairs
                                .add(new NodeDiffPair(nodePerLineBefore.get(index),
                                        getPairOf(nodePerLineBefore.get(index), diffPairs), DiffType.UPDATE));

                    } else if (action instanceof Move) {
                        diffPairs
                                .add(new NodeDiffPair(nodePerLineBefore.get(index),
                                        getPairOf(nodePerLineBefore.get(index), diffPairs), DiffType.MOVE));
                    } else {
                        insertAndRemoveActions.add(action);
                    }
                } catch (PairException e) {
                    System.out.println(action.getName());
                    System.out.println(action.getNode().getClass());
                    System.out.println(action.getClass());

                    if (!action.toString().contains("Block")) {
                        System.out.println("Warning: " + e.getMessage() + "\n" + action.toString());
                        System.exit(0);
                    }

                }

            }

            for (Action action : insertAndRemoveActions) {
                if (!isValidNode(action.getNode())) {
                    continue;
                }
                try {
                    String index = action.getNode().getType().toString() + action.getNode().getPos();
                    if (action instanceof Insert || action instanceof TreeInsert) {

                        if (nodePerLineAfter.get(index) == null) {
                            System.out.println(nodePerLineAfter.get(index));
                            System.out.println(index);
                            System.out.println(action.getNode());
                            System.out.println("updated");
                        }

                        diffPairs
                                .add(new NodeDiffPair(null, nodePerLineAfter.get(index), DiffType.INSERT));
                    } else if (action instanceof Delete || action instanceof TreeDelete) {
                        diffPairs
                                .add(new NodeDiffPair(nodePerLineBefore.get(index), null, DiffType.REMOVE));
                    } else if (action instanceof Update && ignoreUpdate) { // Ignoring update action
                        diffPairs
                                .add(new NodeDiffPair(null,
                                        getPairOf(nodePerLineBefore.get(index), diffPairs), DiffType.INSERT));
                        diffPairs
                                .add(new NodeDiffPair(nodePerLineBefore.get(index), null, DiffType.REMOVE));
                    } else {
                        throw new Exception("Detected action not linked\n" + action.toString());
                    }
                } catch (PairException e) {
                    System.out.println("Warning2: " + e.getMessage() + "\n" + action.toString());
                }
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            e.printStackTrace();

        }

        // FIXME: Aparentemente não está mais sendo necessário, causava duplicidade (add
        // e rmv do mesmo elemento)
        // int pairsNumber = diffPairs.size();
        // for (int i = 0; i < pairsNumber; i++) {
        // NodeDiffPair diffPair = diffPairs.get(i);
        // if (diffPair.getDiffType().equals(DiffType.INSERT) &&
        // diffPair.getAfter().getNode() instanceof Block) {
        // diffPairs.addAll(openInsertedBlock(diffPair));
        // } else if (diffPair.getDiffType().equals(DiffType.REMOVE)
        // && diffPair.getBefore().getNode() instanceof Block) {
        // diffPairs.addAll(openRemovedBlock(diffPair));
        // } else if (diffPair.getBefore() != null && diffPair.getAfter() != null
        // && diffPair.getBefore().getNode() instanceof Block) {
        // diffPairs.addAll(openPairedBlock(diffPair));
        // }
        // }

        diffPairs.forEach(diffPair -> {
            diffPair.validateMetrics();
        });
        collect(fileDiff, diffPairs);

    }

    private List<NodeDiffPair> openInsertedBlock(NodeDiffPair diffPair) {
        ArrayList<NodeDiffPair> openedBlock = new ArrayList<>();
        diffPair.getAfter().getChildren().forEach((child) -> {
            try {
                NodeDiffPair newdiff = new NodeDiffPair(null, child, DiffType.INSERT);
                openedBlock.add(newdiff);
                openedBlock.addAll(openInsertedBlock(newdiff));
            } catch (Exception e) {
                // Never gonna happen
                e.printStackTrace();
            }

        });

        return openedBlock;
    }

    private List<NodeDiffPair> openRemovedBlock(NodeDiffPair diffPair) {
        ArrayList<NodeDiffPair> openedBlock = new ArrayList<>();
        diffPair.getBefore().getChildren().forEach((child) -> {
            try {
                NodeDiffPair newdiff = new NodeDiffPair(child, null, DiffType.REMOVE);
                openedBlock.add(newdiff);
                openedBlock.addAll(openRemovedBlock(newdiff));
            } catch (Exception e) {
                // Never gonna happen
                e.printStackTrace();
            }

        });

        return openedBlock;
    }

    private List<NodeDiffPair> openPairedBlock(NodeDiffPair diffPair) {
        ArrayList<NodeDiffPair> openedBlock = new ArrayList<>();
        // Equals and removed
        diffPair.getBefore().getChildren().forEach((child) -> {

            List<TreeASTNode<? extends ASTNode>> pairs = diffPair.getAfter().getChildren().stream()
                    .filter(e -> e.equals(child)).toList();

            if (pairs.size() == 1) {
                try {

                    NodeDiffPair newdiff = new NodeDiffPair(child, pairs.get(0), DiffType.UPDATE);
                    openedBlock.add(newdiff);
                    openedBlock.addAll(openPairedBlock(newdiff));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    NodeDiffPair newdiff = new NodeDiffPair(child, null, DiffType.REMOVE);
                    openedBlock.add(newdiff);
                    openedBlock.addAll(openRemovedBlock(newdiff));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Add
        diffPair.getAfter().getChildren().forEach((child) -> {

            List<TreeASTNode<? extends ASTNode>> pairs = diffPair.getBefore().getChildren().stream()
                    .filter(e -> e.equals(child)).toList();

            if (pairs.size() == 0) {
                try {
                    NodeDiffPair newdiff = new NodeDiffPair(null, child, DiffType.INSERT);
                    openedBlock.add(newdiff);
                    openedBlock.addAll(openInsertedBlock(newdiff));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return openedBlock;
    }

    public abstract void collect(FileDiff fileDiff, ArrayList<NodeDiffPair> diff);

    private TreeASTNode getPairOf(TreeASTNode javaASTNode, ArrayList<NodeDiffPair> diff) throws PairException {

        List<NodeDiffPair> pairs = diff.stream().filter(pair -> pair.getDiffType().equals(DiffType.PAIR))
                .toList();
        for (int i = 0; i < pairs.size(); i++) {
            NodeDiffPair pair = pairs.get(i);
            if (pair.getBefore().equals(javaASTNode)) {
                return pair.getAfter();
            }
        }
        throw new PairException("Action without pair");
    }

    private boolean isValidNode(Tree tree) {
        // Operadores exclusivos do GUMTREE
        if (tree.getType().toString().equalsIgnoreCase("INFIX_EXPRESSION_OPERATOR") ||
                tree.getType().toString().equalsIgnoreCase("VARARGS_TYPE") ||
                tree.getType().toString().equalsIgnoreCase("PREFIX_EXPRESSION_OPERATOR") ||
                tree.getType().toString().equalsIgnoreCase("POSTFIX_EXPRESSION_OPERATOR") ||
                tree.getType().toString().equalsIgnoreCase("ASSIGNMENT_OPERATOR") ||
                tree.getType().toString().equalsIgnoreCase("TYPE_DECLARATION_KIND") ||
                tree.getType().toString().equalsIgnoreCase("METHOD_INVOCATION_RECEIVER") ||
                tree.getType().toString().equalsIgnoreCase("METHOD_INVOCATION_ARGUMENTS")) {
            return false;
        }
        return true;
    }

    private HashMap<String, TreeASTNode> computeNodePosition(TreeASTNode astNode,
            HashMap<String, TreeASTNode> nodesPerPosition) {
        nodesPerPosition.put(astNode.getNodeType() + astNode.getNode().getStartPosition(), astNode);
        astNode.getChildren().forEach(child -> {
            computeNodePosition((TreeASTNode<ASTNode>) child, nodesPerPosition);
        });
        return nodesPerPosition;
    }

}
