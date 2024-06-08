package lab.ref.customrefactoring.model;

public class RefactoringElement {

    private String mainClass;
    private String file;
    private String element;


    public RefactoringElement(String mainClass, String file, String element) {
        this.mainClass = mainClass;
        this.file = file;
        this.element = element;
    }

    public String getFile() {
        return this.file;
    }

    public void setFile(String file) {
        this.file = file;
    }


    public String getMainClass() {
        return this.mainClass;
    }

    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }

    public String getElement() {
        return this.element;
    }

    public void setElement(String element) {
        this.element = element;
    }


    @Override
    public String toString() {
        return "{" +
            " mainClass='" + getMainClass() + "'" +
            ", file='" + getFile() + "'" +
            ", element='" + getElement() + "'" +
            "}";
    }


}
