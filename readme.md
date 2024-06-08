Data used in the study: https://drive.google.com/file/d/13ZXKbN9z2a_xINJ-CilnuKZ34ZqPDsGu/view?usp=sharing

## **The tool is made up of 2 projects.**

 **ChangeMetricsMiner** is responsible for assembling AST, comparing and generating code modifications between two versions of a file or commit.

**CustomRefactoringMiner** is responsible for detecting instances that refactoring has occurred using RefMiner. It will run ChangeMetricsMiner for each detected instance and filter/report code modifications based on the detected refactoring.

Configuration (Java openjdk 17.0.8.1 2023-08-24 LTS was used):

1- Clone the repository

	git clone https://github.com/refstudy/ase2024refactoring.git

2- Install the dependencies in both projects by running install in each root folder

    mvn install

2.1- Opening both projects in the same IDE studio will cause changes to ChangeMetricsMiner to be detected by CustomRefactoringMiner without a new install being necessary.

3- Adjust the settings in the config.json file of the CustomRefactoringMiner project. It is necessary to define a temporary folder to keep the cloned projects and an output folder for the output. Finally, for each project, simply enter the .git url and the branch to be collected.

    {
    
	    "tempFoldPath": ".\\projetos",
	    "outputFoldPath": ".\\data",
	    "repositories": [
			    {
			    "url": "https://github.com/jwtk/jjwt.git",
			    "mainBranch": "master",
			    },
		]
    }

4- Run the App.java in order to experiment the tool



Output example:

    [
        {
            "commit": "0ae8d9f3b740dff1a82fb8e4d1d0afc8cff1c7a8",
            "sourceElements": "[org.activiti.engine.FormService.addComment(String, String, String)]",
            "targetElements": "[org.activiti.engine.TaskService.addComment(String, String, String)]",
            "type": "MOVE_METHOD",
            "related": [
                {
                    "Metric": "ADDED_METHOD",
                    "Method": "org.activiti.engine.TaskService",
                    "Local": "L[268,269], C[3,75]",
                    "Detail": "org.activiti.engine.TaskService.addComment(String, String, String)",
                    "Code": "/** \n * Add a comment to a task and/or process instance. \n */\nvoid addComment(String taskId,String processInstanceId,String message);\n",
                    "relationType": "MENTIONED_DECLARATION",
                    "distance": 0
                },
                {
                    "Metric": "REMOVED_METHOD",
                    "Method": "org.activiti.engine.FormService",
                    "Local": "L[76,77], C[3,75]",
                    "Detail": "org.activiti.engine.FormService.addComment(String, String, String)",
                    "Code": "/** \n * Add a comment to a task and/or process instance. \n */\nvoid addComment(String taskId,String processInstanceId,String message);\n",
                    "relationType": "MENTIONED_DECLARATION",
                    "distance": 0
                },
                {
                    "Metric": "REMOVED_Javadoc",
                    "Method": "org.activiti.engine.FormService.addComment(String, String, String)",
                    "Local": "L[76,76], C[3,57]",
                    "Detail": null,
                    "Code": "/** \n * Add a comment to a task and/or process instance. \n */\n",
                    "relationType": "MENTIONED",
                    "distance": 0
                },
                {
                    "Metric": "REMOVED_TextElement",
                    "Method": "org.activiti.engine.FormService.addComment(String, String, String)",
                    "Local": "L[76,76], C[7,55]",
                    "Detail": null,
                    "Code": "Add a comment to a task and/or process instance. ",
                    "relationType": "MENTIONED",
                    "distance": 0
                },
                {
                    "Metric": "REMOVED_TagElement",
                    "Method": "org.activiti.engine.FormService.addComment(String, String, String)",
                    "Local": "L[76,76], C[7,55]",
                    "Detail": null,
                    "Code": "\n * Add a comment to a task and/or process instance. ",
                    "relationType": "MENTIONED",
                    "distance": 0
                },
                {
                    "Metric": "ADDED_Javadoc",
                    "Method": "org.activiti.engine.TaskService.addComment(String, String, String)",
                    "Local": "L[268,268], C[3,57]",
                    "Detail": null,
                    "Code": "/** \n * Add a comment to a task and/or process instance. \n */\n",
                    "relationType": "MENTIONED",
                    "distance": 0
                },
                {
                    "Metric": "ADDED_TextElement",
                    "Method": "org.activiti.engine.TaskService.addComment(String, String, String)",
                    "Local": "L[268,268], C[7,55]",
                    "Detail": null,
                    "Code": "Add a comment to a task and/or process instance. ",
                    "relationType": "MENTIONED",
                    "distance": 0
                },
                {
                    "Metric": "ADDED_TagElement",
                    "Method": "org.activiti.engine.TaskService.addComment(String, String, String)",
                    "Local": "L[268,268], C[7,55]",
                    "Detail": null,
                    "Code": "\n * Add a comment to a task and/or process instance. ",
                    "relationType": "MENTIONED",
                    "distance": 0
                }
            ],
            "notRelated": [
                {
                    "Metric": "REMOVED_Javadoc",
                    "Method": "org.activiti.engine.FormService.getTaskComments(String)",
                    "Local": "L[79,79], C[3,48]",
                    "Detail": null,
                    "Code": "/** \n * The comments related to the given task. \n */\n",
                    "relationType": "NOT_RELATED",
                    "distance": 99
                },
                {
                    "Metric": "REMOVED_ParameterizedType",
                    "Method": "org.activiti.engine.FormService.getTaskComments(String)",
                    "Local": "L[80,80], C[3,15]",
                    "Detail": null,
                    "Code": "List<Comment>",
                    "relationType": "NOT_RELATED",
                    "distance": 99
                },
    
                {
                    "Metric": "REMOVED_ParameterizedType",
                    "Method": "org.activiti.engine.FormService.getProcessInstanceComments(String)",
                    "Local": "L[83,83], C[3,15]",
                    "Detail": null,
                    "Code": "List<Comment>",
                    "relationType": "NOT_RELATED",
                    "distance": 99
                },
     
            ],
            "metadata": {
                "type": "Move Method",
                "description": "Move Method public addComment(taskId String, processInstanceId String, message String) : void from class org.activiti.engine.FormService to public addComment(taskId String, processInstanceId String, message String) : void from class org.activiti.engine.TaskService",
                "leftSideLocations": [
                    {
                        "filePath": "modules/activiti-engine/src/main/java/org/activiti/engine/FormService.java",
                        "startLine": 76,
                        "endLine": 77,
                        "startColumn": 3,
                        "endColumn": 76,
                        "codeElementType": "METHOD_DECLARATION",
                        "description": "original method declaration",
                        "codeElement": "public addComment(taskId String, processInstanceId String, message String) : void"
                    }
                ],
                "rightSideLocations": [
                    {
                        "filePath": "modules/activiti-engine/src/main/java/org/activiti/engine/TaskService.java",
                        "startLine": 268,
                        "endLine": 269,
                        "startColumn": 3,
                        "endColumn": 76,
                        "codeElementType": "METHOD_DECLARATION",
                        "description": "moved method declaration",
                        "codeElement": "public addComment(taskId String, processInstanceId String, message String) : void"
                    }
                ]
            }
        }
    
    ]

