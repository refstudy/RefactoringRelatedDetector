package lab.ref.customrefactoring.minerhandler;

import java.util.List;

import lab.ref.customrefactoring.model.RefactoringInstance;
import lab.ref.customrefactoring.model.RepositoryStatus;

public interface IRefactoringAction {

    void onCollect(RefactoringCollectorTool tool, RepositoryStatus repo, String commit, List<RefactoringInstance> instance);
    
} 