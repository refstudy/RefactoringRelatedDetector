package org.metricsminer.metric.changemetricsgenerator;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.ASTNode;
import org.metricsminer.model.astnodes.TreeASTNode;
import org.metricsminer.model.diff.NodeDiffPair;
import org.metricsminer.model.diff.NodeDiffPair.DiffType;
import org.metricsminer.util.Action;

import com.google.gson.Gson;

public abstract class ChangeMetricGenerator<T> {

    protected final Gson gson = new Gson();

    protected Class<T> nodeType;

    public ChangeMetricGenerator(Class<T> nodeType) {
        this.nodeType = nodeType;
    }

    @SuppressWarnings("unchecked")
    public ChangeMetricGenerator() {
        try {
            this.nodeType = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
                    .getActualTypeArguments()[0];
        } catch (Exception e) {
            // e.printStackTrace();
        }
    }

    protected List<ChangeMetric> insertAction(NodeDiffPair diffPair, T insertedObj) {
        return new ArrayList<>();
    }

    protected List<ChangeMetric> removeAction(NodeDiffPair diffPair, T removedObj) {
        return new ArrayList<>();
    }

    protected List<ChangeMetric> pairAction(NodeDiffPair diffPair, T beforeObj, T afterObj) {
        return new ArrayList<>();
    }

    private List<ChangeMetric> insert(NodeDiffPair diffPair, T insertedObj) {
        return insertAction(diffPair, insertedObj)
                .stream()
                .map(me -> {
                    me.setDiffType(DiffType.INSERT);
                    return me;
                })
                .toList();
    }

    private List<ChangeMetric> remove(NodeDiffPair diffPair, T removedObj) {
        return removeAction(diffPair, removedObj)
                .stream()
                .map(me -> {
                    me.setDiffType(DiffType.REMOVE);
                    return me;
                })
                .toList();
    }

    private List<ChangeMetric> pair(NodeDiffPair diffPair, T beforeObj, T afterObj) {
        return pairAction(diffPair, beforeObj, afterObj)
                .stream()
                .map(me -> {
                    if (me.getDiffType() == null) {
                        me.setDiffType(DiffType.PAIR);
                    }
                    return me;
                })
                .toList();
    }

    public List<ChangeMetric> generateFromNodeDiff(NodeDiffPair diffPair) {
        ArrayList<ChangeMetric> metrics = new ArrayList<>();

        TreeASTNode<? extends ASTNode> before = diffPair.getBefore();
        TreeASTNode<? extends ASTNode> after = diffPair.getAfter();

        // INSERT
        if (diffPair.isInsert() && nodeType.isAssignableFrom(getCompareNodeClass(after))) {
            metrics.addAll(insert(diffPair, getCompareNode(after)));
        }
        // REMOVE
        else if (diffPair.isRemove()
                && nodeType.isAssignableFrom(getCompareNodeClass(before))) {
            metrics.addAll(remove(diffPair, getCompareNode(before)));

        }
        // PAIR
        else if ((diffPair.isPair() || diffPair.isUpdate() || diffPair.isMove())
                && nodeType.isAssignableFrom(getCompareNodeClass(after))) {

            if (before.getParent() == null || after.getParent() == null) {
                metrics.addAll(pair(diffPair, getCompareNode(before),
                        getCompareNode(after)));
            } else {
                boolean sameParent = before.getParent().getFullName().equals(after.getParent().getFullName());
                // Se forem de pais diferentes e isso importar, trata como insert e remove
                if (sameParent || (!sameParent && !considerPairLocation())) {
                    metrics.addAll(pair(diffPair, getCompareNode(before),
                            getCompareNode(after)));
                } else {
                    metrics.addAll(insert(diffPair, getCompareNode(after)));
                    metrics.addAll(remove(diffPair, getCompareNode(before)));
                }
            }

        }
        for (ChangeMetric metric : metrics) {
            TreeASTNode<? extends ASTNode> mainNode = diffPair.getAfter();
            mainNode = after;
            if (metric.getDiffType().equals(DiffType.REMOVE)) {
                mainNode = before;
            }
            if (metric.getLine() == -1) {
                metric.setLine(mainNode.getLine());

            }

            if (metric.getEndLine() == -1) {
                metric.setEndLine(mainNode.getEndLine());

            }

            if (metric.getColumn() == -1) {
                metric.setColumn(mainNode.getColumn());

            }

            if (metric.getEndColumn() == -1) {
                metric.setEndColumn(mainNode.getEndColumn());
            }

            if (metric.getNodeDiffPair() == null) {
                metric.setNodeDiffPair(getFirstValidParent(diffPair, metric.getDiffType().equals(DiffType.REMOVE)));
            }

            if (metric.getClosestElement() == null) {
                metric.setClosestElement(mainNode);
            }

            metric.getNodeDiffPair().addChangeMetric(metric);

        }
        return metrics;
    }

    protected boolean considerPairLocation() {
        return true;
    }

    @SuppressWarnings("unchecked")
    protected T getCompareNode(TreeASTNode<? extends ASTNode> treeASTNode) {
        if (TreeASTNode.class.isAssignableFrom(this.nodeType)) {
            return (T) treeASTNode;
        }
        return (T) treeASTNode.getNode();
    }

    protected Class getCompareNodeClass(TreeASTNode<? extends ASTNode> treeASTNode) {
        return this.getCompareNode(treeASTNode).getClass();
    }

    protected ChangeMetric buildChangeMetric(String name, String description, Object value, int[] line, int[] column,
            final NodeDiffPair toInsertNode, DiffType diffType) {
        ChangeMetric cm = new ChangeMetric(name, description);
        cm.setValue(value);
        cm.setGenerator(getClass().getSimpleName());
        cm.setLine(line[0]);
        cm.setColumn(column[0]);
        cm.setEndLine(line[1]);
        cm.setEndColumn(column[1]);
        cm.setDiffType(diffType);
        cm.setNodeDiffPair(toInsertNode);
        return cm;
    }

    protected ChangeMetric buildChangeMetric(String name, String description, Object value) {
        return buildChangeMetric(name, description, value, new int[] { -1, -1 }, new int[] { -1, -1 }, null, null);
    }

    protected ChangeMetric buildChangeMetric(String name, String description, Object value,
            final NodeDiffPair toInsertNode) {
        return buildChangeMetric(name, description, value, new int[] { -1, -1 }, new int[] { -1, -1 }, toInsertNode,
                toInsertNode.getDiffType());
    }

    public static NodeDiffPair getFirstValidParent(NodeDiffPair diffPair) {

        return getFirstValidParent(diffPair, false);

    }

    public static NodeDiffPair getFirstValidParent(NodeDiffPair diffPair, boolean isRemove) {

        TreeASTNode firstValidParent;
        if (isRemove || diffPair.getAfter() == null) {
            // Removed the entire file
            if (diffPair.getBefore().isRoot()) {
                return diffPair;
            }
            firstValidParent = diffPair.getBefore().getParent();

        } else {
            // Added the entire file
            if (diffPair.getAfter().isRoot()) {
                return diffPair;
            }
            // FIXME(Prioridade): caso do fixme abaixo é quando entra aqui
            firstValidParent = diffPair.getAfter().getParent();

        }
        /*
         * FIXME(Prioridade):
         * Avaliar se quando o nó que tem uma versão after (depois da refactoring), pq
         * ele não tem um diff (NodeDiffPair==null) em alguns casos? Sendo necessário
         * subir a árvore até achar um que tenha um NodeDiffPair
         */
        while (firstValidParent.getNodeDiffPair() == null) {
            // Removed the entire file?
            if (firstValidParent.isRoot()) {
                // return null;
                return diffPair;
            }
            firstValidParent = firstValidParent.getParent();

        }
        return firstValidParent.getNodeDiffPair();
    }

    // public static NodeDiffPair getFirstValidParent(NodeDiffPair diffPair, boolean
    // isRemove) {

    // if (diffPair.getAfter() == null && diffPair.getBefore() == null) {
    // System.out.println("tudo null");
    // return null;
    // }
    // if (diffPair.getAfter() != null && !isRemove) {
    // if(diffPair.getAfter().getParent().getNodeDiffPair() == null){
    // System.out.println("pai do after null");
    // System.out.println(diffPair.getAfter().getParent());
    // }
    // return diffPair.getAfter().getParent().getNodeDiffPair();
    // }

    // // Removed the entire file
    // if (diffPair.getBefore().isRoot()) {
    // // return null;
    // return diffPair;
    // }
    // TreeASTNode firstValidParent = diffPair.getBefore().getParent();
    // while (firstValidParent.getNodeDiffPair() == null) {
    // // Removed the entire file?
    // if (firstValidParent.isRoot()) {
    // // return null;
    // return diffPair;
    // }
    // firstValidParent = firstValidParent.getParent();

    // }
    // return firstValidParent.getNodeDiffPair();
    // }

    protected <T> void actionOverUniqueString(List<T> list1, List<T> list2, Action<T> action1,
            Action<T> action2) {

        HashMap<String, T> originalMap = new HashMap<>();
        list1.forEach(e -> originalMap.put("l1_" + e.toString(), e));
        list2.forEach(e -> originalMap.put("l2_" + e.toString(), e));
        // Encontrar elementos únicos de cada lista
        Set<String> uniqueList1 = new HashSet<>(list1.stream().map(Object::toString).toList());
        uniqueList1.removeAll(list2.stream().map(Object::toString).toList());

        Set<String> uniqueList2 = new HashSet<>(list2.stream().map(Object::toString).toList());
        uniqueList2.removeAll(list1.stream().map(Object::toString).toList());
        // Executar ação em cada elemento único da lista1
        try {
            for (String element : uniqueList1) {
                // Executar ação desejada
                action1.doAction(originalMap.get("l1_" + element));
            }

            // Executar ação em cada elemento único da lista2
            for (String element : uniqueList2) {
                // Executar ação desejada
                action2.doAction(originalMap.get("l2_" + element));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
