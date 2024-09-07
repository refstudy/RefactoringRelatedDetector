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

## **List of projects**

1. **KunMinX-Jetpack-MVVM-Best-Practice** (https://github.com/KunMinX/Jetpack-MVVM-Best-Practice.git)
2. **square-retrofit** (https://github.com/square/retrofit.git)
3. **NationalSecurityAgency-ghidra** (https://github.com/NationalSecurityAgency/ghidra.git)
4. **PhilJay-MPAndroidChart** (https://github.com/PhilJay/MPAndroidChart.git)
5. **jeecgboot-jeecg-boot** (https://github.com/jeecgboot/jeecg-boot.git)
6. **skylot-jadx** (https://github.com/skylot/jadx.git)
7. **airbnb-lottie-android** (https://github.com/airbnb/lottie-android.git)
8. **bumptech-glide** (https://github.com/bumptech/glide.git)
9. **alibaba-arthas** (https://github.com/alibaba/arthas.git)
10. **Blankj-AndroidUtilCode** (https://github.com/Blankj/AndroidUtilCode.git)
11. **zxing-zxing** (https://github.com/zxing/zxing.git)
12. **xkcoding-spring-boot-demo** (https://github.com/xkcoding/spring-boot-demo.git)
13. **apolloconfig-apollo** (https://github.com/apolloconfig/apollo.git)
14. **alibaba-easyexcel** (https://github.com/alibaba/easyexcel.git)
15. **alibaba-canal** (https://github.com/alibaba/canal.git)
16. **alibaba-spring-cloud-alibaba** (https://github.com/alibaba/spring-cloud-alibaba.git)
17. **JakeWharton-butterknife** (https://github.com/JakeWharton/butterknife.git)
18. **alibaba-fastjson** (https://github.com/alibaba/fastjson.git)
19. **xuxueli-xxl-job** (https://github.com/xuxueli/xxl-job.git)
20. **greenrobot-EventBus** (https://github.com/greenrobot/EventBus.git)
21. **scwang90-SmartRefreshLayout** (https://github.com/scwang90/SmartRefreshLayout.git)
22. **seata-seata** (https://github.com/seata/seata.git)
23. **Netflix-Hystrix** (https://github.com/Netflix/Hystrix.git)
24. **termux-termux-app** (https://github.com/termux/termux-app.git)
25. **google-gson** (https://github.com/google/gson.git)
26. **elunez-eladmin** (https://github.com/elunez/eladmin.git)
27. **didi-DoKit** (https://github.com/didi/DoKit.git)
28. **apache-rocketmq** (https://github.com/apache/rocketmq.git)
29. **CarGuo-GSYVideoPlayer** (https://github.com/CarGuo/GSYVideoPlayer.git)
30. **mybatis-mybatis-3** (https://github.com/mybatis/mybatis-3.git)
31. **brettwooldridge-HikariCP** (https://github.com/brettwooldridge/HikariCP.git)
32. **facebook-fresco** (https://github.com/facebook/fresco.git)
33. **Tencent-tinker** (https://github.com/Tencent/tinker.git)
34. **YunaiV-ruoyi-vue-pro** (https://github.com/YunaiV/ruoyi-vue-pro.git)
35. **shuzheng-zheng** (https://github.com/shuzheng/zheng.git)
36. **iBotPeaches-Apktool** (https://github.com/iBotPeaches/Apktool.git)
37. **openzipkin-zipkin** (https://github.com/openzipkin/zipkin.git)
38. **LMAX-Exchange-disruptor** (https://github.com/LMAX-Exchange/disruptor.git)
39. **Tencent-APIJSON** (https://github.com/Tencent/APIJSON.git)
40. **williamfiset-Algorithms** (https://github.com/williamfiset/Algorithms.git)
41. **justauth-JustAuth** (https://github.com/justauth/JustAuth.git)
42. **Tencent-QMUI_Android** (https://github.com/Tencent/QMUI_Android.git)
43. **heibaiying-BigData-Notes** (https://github.com/heibaiying/BigData-Notes.git)
44. **Konloch-bytecode-viewer** (https://github.com/Konloch/bytecode-viewer.git)
45. **ben-manes-caffeine** (https://github.com/ben-manes/caffeine.git)
46. **Grasscutters-Grasscutter** (https://github.com/Grasscutters/Grasscutter.git)
47. **deeplearning4j-deeplearning4j** (https://github.com/deeplearning4j/deeplearning4j.git)
48. **GoogleContainerTools-jib** (https://github.com/GoogleContainerTools/jib.git)
49. **Netflix-zuul** (https://github.com/Netflix/zuul.git)
50. **dromara-Sa-Token** (https://github.com/dromara/Sa-Token.git)
51. **LuckSiege-PictureSelector** (https://github.com/LuckSiege/PictureSelector.git)
52. **YunaiV-yudao-cloud** (https://github.com/YunaiV/yudao-cloud.git)
53. **macrozheng-mall-learning** (https://github.com/macrozheng/mall-learning.git)
54. **projectlombok-lombok** (https://github.com/projectlombok/lombok.git)
55. **google-guice** (https://github.com/google/guice.git)
56. **kunal-kushwaha-DSA-Bootcamp-Java** (https://github.com/kunal-kushwaha/DSA-Bootcamp-Java.git)
57. **Netflix-eureka** (https://github.com/Netflix/eureka.git)
58. **codecentric-spring-boot-admin** (https://github.com/codecentric/spring-boot-admin.git)
59. **pagehelper-Mybatis-PageHelper** (https://github.com/pagehelper/Mybatis-PageHelper.git)
60. **apache-zookeeper** (https://github.com/apache/zookeeper.git)
61. **redis-jedis** (https://github.com/redis/jedis.git)
62. **barry-ran-QtScrcpy** (https://github.com/barry-ran/QtScrcpy.git)
63. **pxb1988-dex2jar** (https://github.com/pxb1988/dex2jar.git)
64. **gyoogle-tech-interview-for-developer** (https://github.com/gyoogle/tech-interview-for-developer.git)
65. **code4craft-webmagic** (https://github.com/code4craft/webmagic.git)
66. **mission-peace-interview** (https://github.com/mission-peace/interview.git)
67. **android-async-http-android-async-http** (https://github.com/android-async-http/android-async-http.git)
68. **LSPosed-LSPosed** (https://github.com/LSPosed/LSPosed.git)
69. **square-javapoet** (https://github.com/square/javapoet.git)
70. **JessYanCoding-MVPArms** (https://github.com/JessYanCoding/MVPArms.git)
71. **jhy-jsoup** (https://github.com/jhy/jsoup.git)
72. **google-auto** (https://github.com/google/auto.git)
73. **clojure-clojure** (https://github.com/clojure/clojure.git)
74. **TooTallNate-Java-WebSocket** (https://github.com/TooTallNate/Java-WebSocket.git)
75. **Netflix-conductor** (https://github.com/Netflix/conductor.git)
76. **Activiti-Activiti** (https://github.com/Activiti/Activiti.git)
77. **asLody-VirtualApp** (https://github.com/asLody/VirtualApp.git)
78. **Justson-AgentWeb** (https://github.com/Justson/AgentWeb.git)
79. **OpenFeign-feign** (https://github.com/OpenFeign/feign.git)
80. **resilience4j-resilience4j** (https://github.com/resilience4j/resilience4j.git)
81. **junit-team-junit4** (https://github.com/junit-team/junit4.git)
82. **signalapp-Signal-Server** (https://github.com/signalapp/Signal-Server.git)
83. **plantuml-plantuml** (https://github.com/plantuml/plantuml.git)
84. **zhoutaoo-SpringCloud** (https://github.com/zhoutaoo/SpringCloud.git)
85. **hs-web-hsweb-framework** (https://github.com/hs-web/hsweb-framework.git)
86. **react-native-image-picker-react-native-image-picker** (https://github.com/react-native-image-picker/react-native-image-picker.git)
87. **json-path-JsonPath** (https://github.com/json-path/JsonPath.git)
88. **apache-shardingsphere-elasticjob** (https://github.com/apache/shardingsphere-elasticjob.git)
89. **java-native-access-jna** (https://github.com/java-native-access/jna.git)
90. **apache-shenyu** (https://github.com/apache/shenyu.git)
91. **dropwizard-metrics** (https://github.com/dropwizard/metrics.git)
92. **zfile-dev-zfile** (https://github.com/zfile-dev/zfile.git)
93. **google-android-classyshark** (https://github.com/google/android-classyshark.git)
94. **wildfirechat-im-server** (https://github.com/wildfirechat/im-server.git)
95. **swagger-api-swagger-core** (https://github.com/swagger-api/swagger-core.git)
96. **flyway-flyway** (https://github.com/flyway/flyway.git)
97. **abel533-Mapper** (https://github.com/abel533/Mapper.git)
98. **karatelabs-karate** (https://github.com/karatelabs/karate.git)
99. **li-xiaojun-XPopup** (https://github.com/li-xiaojun/XPopup.git)
100. **Tencent-Shadow** (https://github.com/Tencent/Shadow.git)
101. **naman14-Timber** (https://github.com/naman14/Timber.git)
102. **NanoHttpd-nanohttpd** (https://github.com/NanoHttpd/nanohttpd.git)
103. **zo0r-react-native-push-notification** (https://github.com/zo0r/react-native-push-notification.git)
104. **alibaba-transmittable-thread-local** (https://github.com/alibaba/transmittable-thread-local.git)
105. **apache-storm** (https://github.com/apache/storm.git)
106. **rest-assured-rest-assured** (https://github.com/rest-assured/rest-assured.git)
107. **mrniko-netty-socketio** (https://github.com/mrniko/netty-socketio.git)
108. **ArthurHub-Android-Image-Cropper** (https://github.com/ArthurHub/Android-Image-Cropper.git)
109. **mapstruct-mapstruct** (https://github.com/mapstruct/mapstruct.git)
110. **didi-KnowStreaming** (https://github.com/didi/KnowStreaming.git)
111. **sshahine-JFoenix** (https://github.com/sshahine/JFoenix.git)
112. **AsyncHttpClient-async-http-client** (https://github.com/AsyncHttpClient/async-http-client.git)
113. **provectus-kafka-ui** (https://github.com/provectus/kafka-ui.git)
114. **joelittlejohn-jsonschema2pojo** (https://github.com/joelittlejohn/jsonschema2pojo.git)
115. **gzu-liyujiang-AndroidPicker** (https://github.com/gzu-liyujiang/AndroidPicker.git)
116. **lightbend-config** (https://github.com/lightbend/config.git)
117. **prolificinteractive-material-calendarview** (https://github.com/prolificinteractive/material-calendarview.git)
118. **weibocom-motan** (https://github.com/weibocom/motan.git)
119. **springfox-springfox** (https://github.com/springfox/springfox.git)
120. **graphql-java-graphql-java** (https://github.com/graphql-java/graphql-java.git)
121. **apache-seatunnel** (https://github.com/apache/seatunnel.git)
122. **lets-blade-blade** (https://github.com/lets-blade/blade.git)
123. **haifengl-smile** (https://github.com/haifengl/smile.git)
124. **cabaletta-baritone** (https://github.com/cabaletta/baritone.git)
125. **microg-GmsCore** (https://github.com/microg/GmsCore.git)
126. **quartz-scheduler-quartz** (https://github.com/quartz-scheduler/quartz.git)
127. **wiremock-wiremock** (https://github.com/wiremock/wiremock.git)
128. **amitshekhariitbhu-Fast-Android-Networking** (https://github.com/amitshekhariitbhu/Fast-Android-Networking.git)
129. **Vedenin-useful-java-links** (https://github.com/Vedenin/useful-java-links.git)
130. **btraceio-btrace** (https://github.com/btraceio/btrace.git)
131. **PowerJob-PowerJob** (https://github.com/PowerJob/PowerJob.git)
132. **scribejava-scribejava** (https://github.com/scribejava/scribejava.git)
133. **AriaLyy-Aria** (https://github.com/AriaLyy/Aria.git)
134. **TommyLemon-Android-ZBLibrary** (https://github.com/TommyLemon/Android-ZBLibrary.git)
135. **vavr-io-vavr** (https://github.com/vavr-io/vavr.git)
136. **huanghongxun-HMCL** (https://github.com/huanghongxun/HMCL.git)
137. **Genymobile-gnirehtet** (https://github.com/Genymobile/gnirehtet.git)
138. **mybatis-generator** (https://github.com/mybatis/generator.git)
139. **lettuce-io-lettuce-core** (https://github.com/lettuce-io/lettuce-core.git)
140. **google-google-java-format** (https://github.com/google/google-java-format.git)
141. **razerdp-BasePopup** (https://github.com/razerdp/BasePopup.git)
142. **JodaOrg-joda-time** (https://github.com/JodaOrg/joda-time.git)
143. **OpenTSDB-opentsdb** (https://github.com/OpenTSDB/opentsdb.git)
144. **claritylab-lucida** (https://github.com/claritylab/lucida.git)
145. **spring-projects-spring-data-examples** (https://github.com/spring-projects/spring-data-examples.git)
146. **youlookwhat-CloudReader** (https://github.com/youlookwhat/CloudReader.git)
147. **WeiYe-Jing-datax-web** (https://github.com/WeiYe-Jing/datax-web.git)
148. **pig-mesh-pig** (https://github.com/pig-mesh/pig.git)
149. **spring-cloud-spring-cloud-netflix** (https://github.com/spring-cloud/spring-cloud-netflix.git)
150. **sofastack-sofa-boot** (https://github.com/sofastack/sofa-boot.git)
151. **jankotek-mapdb** (https://github.com/jankotek/mapdb.git)
152. **StarRocks-starrocks** (https://github.com/StarRocks/starrocks.git)
153. **coobird-thumbnailator** (https://github.com/coobird/thumbnailator.git)
154. **web3j-web3j** (https://github.com/web3j/web3j.git)
155. **natario1-CameraView** (https://github.com/natario1/CameraView.git)
156. **cglib-cglib** (https://github.com/cglib/cglib.git)
157. **Doikki-DKVideoPlayer** (https://github.com/Doikki/DKVideoPlayer.git)
158. **obsidiandynamics-kafdrop** (https://github.com/obsidiandynamics/kafdrop.git)
159. **Javen205-IJPay** (https://github.com/Javen205/IJPay.git)
160. **brianfrankcooper-YCSB** (https://github.com/brianfrankcooper/YCSB.git)
161. **firebase-FirebaseUI-Android** (https://github.com/firebase/FirebaseUI-Android.git)
162. **citerus-dddsample-core** (https://github.com/citerus/dddsample-core.git)
163. **j-easy-easy-rules** (https://github.com/j-easy/easy-rules.git)
164. **Netflix-ribbon** (https://github.com/Netflix/ribbon.git)
165. **JSQLParser-JSqlParser** (https://github.com/JSQLParser/JSqlParser.git)
166. **CellularPrivacy-Android-IMSI-Catcher-Detector** (https://github.com/CellularPrivacy/Android-IMSI-Catcher-Detector.git)
167. **xuexiangjys-XUI** (https://github.com/xuexiangjys/XUI.git)
168. **spring-projects-spring-authorization-server** (https://github.com/spring-projects/spring-authorization-server.git)
169. **opengoofy-hippo4j** (https://github.com/opengoofy/hippo4j.git)
170. **stleary-JSON-java** (https://github.com/stleary/JSON-java.git)
171. **line-armeria** (https://github.com/line/armeria.git)
172. **azkaban-azkaban** (https://github.com/azkaban/azkaban.git)
173. **LWJGL-lwjgl3** (https://github.com/LWJGL/lwjgl3.git)
174. **chewiebug-GCViewer** (https://github.com/chewiebug/GCViewer.git)
175. **codingapi-tx-lcn** (https://github.com/codingapi/tx-lcn.git)
176. **dreamhead-moco** (https://github.com/dreamhead/moco.git)
177. **DiUS-java-faker** (https://github.com/DiUS/java-faker.git)
178. **bytedeco-javacpp** (https://github.com/bytedeco/javacpp.git)
179. **zlt2000-microservices-platform** (https://github.com/zlt2000/microservices-platform.git)
180. **jetlinks-jetlinks-community** (https://github.com/jetlinks/jetlinks-community.git)
181. **apache-shiro** (https://github.com/apache/shiro.git)
182. **Freeyourgadget-Gadgetbridge** (https://github.com/Freeyourgadget/Gadgetbridge.git)
183. **spring-cloud-spring-cloud-gateway** (https://github.com/spring-cloud/spring-cloud-gateway.git)
184. **ververica-flink-cdc-connectors** (https://github.com/ververica/flink-cdc-connectors.git)
185. **powermock-powermock** (https://github.com/powermock/powermock.git)
186. **eirslett-frontend-maven-plugin** (https://github.com/eirslett/frontend-maven-plugin.git)
187. **killbill-killbill** (https://github.com/killbill/killbill.git)
188. **dromara-hmily** (https://github.com/dromara/hmily.git)
189. **GeyserMC-Geyser** (https://github.com/GeyserMC/Geyser.git)
190. **oshi-oshi** (https://github.com/oshi/oshi.git)
191. **alibaba-jetcache** (https://github.com/alibaba/jetcache.git)
192. **mybatis-spring-boot-starter** (https://github.com/mybatis/spring-boot-starter.git)
193. **jboss-javassist-javassist** (https://github.com/jboss-javassist/javassist.git)
194. **apache-nifi** (https://github.com/apache/nifi.git)
195. **rubenlagus-TelegramBots** (https://github.com/rubenlagus/TelegramBots.git)
196. **discord-jda-JDA** (https://github.com/discord-jda/JDA.git)
197. **rovo89-XposedInstaller** (https://github.com/rovo89/XposedInstaller.git)
198. **osmandapp-OsmAnd** (https://github.com/osmandapp/OsmAnd.git)
199. **zq2599-blog_demos** (https://github.com/zq2599/blog_demos.git)
200. **sofastack-sofa-rpc** (https://github.com/sofastack/sofa-rpc.git)
201. **nayuki-QR-Code-generator** (https://github.com/nayuki/QR-Code-generator.git)
202. **killme2008-aviatorscript** (https://github.com/killme2008/aviatorscript.git)
203. **zendesk-maxwell** (https://github.com/zendesk/maxwell.git)
204. **jacoco-jacoco** (https://github.com/jacoco/jacoco.git)
205. **apache-incubator-heron** (https://github.com/apache/incubator-heron.git)
206. **ikarus23-MifareClassicTool** (https://github.com/ikarus23/MifareClassicTool.git)
207. **davideas-FlexibleAdapter** (https://github.com/davideas/FlexibleAdapter.git)
208. **lilishop-lilishop** (https://github.com/lilishop/lilishop.git)
209. **DrKLO-Telegram** (https://github.com/DrKLO/Telegram)
210. **bytedeco-javacv** (https://github.com/bytedeco/javacv)
211. **CaffeineMC-sodium-fabric** (https://github.com/CaffeineMC/sodium-fabric)
212. **alibaba-Sentinel** (https://github.com/alibaba/Sentinel)
213. **dromara-lamp-cloud** (https://github.com/dromara/lamp-cloud)



## **Change Models**


List of change models detected in the study

	   "ACCESS_INCREASED",
	   "ACCESS_REDUCED",
	   "ADDED_ANNOTATION",
	   "ADDED_ASSERTSTATEMENT",
	   "ADDED_BREAKSTATEMENT",
	   "ADDED_CATCH_CLAUSE",
	   "ADDED_CLASS",
	   "ADDED_CLASS_ANONYMOUS_DECLARATION",
	   "ADDED_CLASS_INSTANCE",
	   "ADDED_CONSTRUCTORINVOCATION",
	   "ADDED_CONTINUESTATEMENT",
	   "ADDED_DOSTATEMENT",
	   "ADDED_ELSE_STATEMENT",
	   "ADDED_ENHANCEDFORSTATEMENT",
	   "ADDED_ENUM",
	   "ADDED_EXCEPTION_THROWN",
	   "ADDED_EXPRESSIONSTATEMENT",
	   "ADDED_FORSTATEMENT",
	   "ADDED_HIERARCHY",
	   "ADDED_IF_STATEMENT",
	   "ADDED_IMPORT",
	   "ADDED_INTERFACE",
	   "ADDED_LABELEDSTATEMENT",
	   "ADDED_METHOD",
	   "ADDED_METHOD_CALL",
	   "ADDED_PARAMETER",
	   "ADDED_RECORD",
	   "ADDED_RETURN_VALUE",
	   "ADDED_SUPERCONSTRUCTORINVOCATION",
	   "ADDED_SWITCHCASE",
	   "ADDED_SWITCHSTATEMENT",
	   "ADDED_SYNCHRONIZEDSTATEMENT",
	   "ADDED_THROWSTATEMENT",
	   "ADDED_TRYSTATEMENT",
	   "ADDED_TRY_CATCH",
	   "ADDED_VARIABLE",
	   "ADDED_WHILESTATEMENT",
	   "ADDED_YIELDSTATEMENT",
	   "CATCH_CLAUSE_CHANGE",
	   "CATCH_HANDLING_CHANGE",
	   "CHANGED_ANONYMOUS_DECLARATION",
	   "CHANGED_ASSERTSTATEMENT",
	   "CHANGED_BREAKSTATEMENT",
	   "CHANGED_CLASS",
	   "CHANGED_CONSTRUCTORINVOCATION",
	   "CHANGED_CONTINUESTATEMENT",
	   "CHANGED_DOSTATEMENT",
	   "CHANGED_ELSE_CONDITION",
	   "CHANGED_ENHANCEDFORSTATEMENT",
	   "CHANGED_ENUM",
	   "CHANGED_EXISTING_ELSE_BLOCK",
	   "CHANGED_EXISTING_IF_BLOCK",
	   "CHANGED_EXPRESSIONSTATEMENT",
	   "CHANGED_FORSTATEMENT",
	   "CHANGED_HIERARCHY",
	   "CHANGED_IF_CONDITION",
	   "CHANGED_IMPORT",
	   "CHANGED_INTERFACE",
	   "CHANGED_LABELEDSTATEMENT",
	   "CHANGED_RECORD",
	   "CHANGED_RETURN_TYPE",
	   "CHANGED_RETURN_VALUE",
	   "CHANGED_SUPERCONSTRUCTORINVOCATION",
	   "CHANGED_SWITCHCASE",
	   "CHANGED_SWITCHSTATEMENT",
	   "CHANGED_SYNCHRONIZEDSTATEMENT",
	   "CHANGED_THROWSTATEMENT",
	   "CHANGED_TRYSTATEMENT",
	   "CHANGED_VARIABLE",
	   "CHANGED_VAR_VALUE",
	   "CHANGED_WHILESTATEMENT",
	   "CHANGED_YIELDSTATEMENT",
	   "CLASS_INSTANCE_ARGUMENTS_CHANGE",
	   "METHOD_CALL_ARGUMENTS_CHANGE",
	   "REMOVED_ANNOTATION",
	   "REMOVED_ASSERTSTATEMENT",
	   "REMOVED_BREAKSTATEMENT",
	   "REMOVED_CATCH_CLAUSE",
	   "REMOVED_CLASS",
	   "REMOVED_CLASS_ANONYMOUS_DECLARATION",
	   "REMOVED_CLASS_INSTANCE",
	   "REMOVED_CONSTRUCTORINVOCATION",
	   "REMOVED_CONTINUESTATEMENT",
	   "REMOVED_DOSTATEMENT",
	   "REMOVED_ELSE_STATEMENT",
	   "REMOVED_ENHANCEDFORSTATEMENT",
	   "REMOVED_ENUM",
	   "REMOVED_EXCEPTION_THROWN",
	   "REMOVED_EXPRESSIONSTATEMENT",
	   "REMOVED_FORSTATEMENT",
	   "REMOVED_HIERARCHY",
	   "REMOVED_IF_STATEMENT",
	   "REMOVED_IMPORT",
	   "REMOVED_INTERFACE",
	   "REMOVED_LABELEDSTATEMENT",
	   "REMOVED_METHOD",
	   "REMOVED_METHOD_CALL",
	   "REMOVED_PARAMETER",
	   "REMOVED_RECORD",
	   "REMOVED_RETURN_VALUE",
	   "REMOVED_SUPERCONSTRUCTORINVOCATION",
	   "REMOVED_SWITCHCASE",
	   "REMOVED_SWITCHSTATEMENT",
	   "REMOVED_SYNCHRONIZEDSTATEMENT",
	   "REMOVED_THROWSTATEMENT",
	   "REMOVED_TRYSTATEMENT",
	   "REMOVED_TRY_CATCH",
	   "REMOVED_VARIABLE",
	   "REMOVED_WHILESTATEMENT",
	   "REMOVED_YIELDSTATEMENT",
	   "RENAME_ELEMENT",
	   "TRY_CATCH_CHANGE",
	   "TURNED_INTO_CLASS",
	   "TURNED_INTO_INTERFACE",
	   "VAR_TYPE_CHANGED"

Based on AST

        "ADDED_AnnotationTypeDeclaration",
        "ADDED_AnnotationTypeMemberDeclaration",
        "ADDED_AnonymousClassDeclaration",
        "ADDED_ArrayAccess",
        "ADDED_ArrayCreation",
        "ADDED_ArrayInitializer",
        "ADDED_ArrayType",
        "ADDED_BooleanLiteral",
        "ADDED_CastExpression",
        "ADDED_CatchClause",
        "ADDED_CharacterLiteral",
        "ADDED_ClassInstanceCreation",
        "ADDED_ConditionalExpression",
        "ADDED_CreationReference",
        "ADDED_Dimension",
        "ADDED_EnumConstantDeclaration",
        "ADDED_EnumDeclaration",
        "ADDED_ExpressionMethodReference",
        "ADDED_FieldAccess",
        "ADDED_FieldDeclaration",
        "ADDED_ImportDeclaration",
        "ADDED_InfixExpression",
        "ADDED_Initializer",
        "ADDED_InstanceofExpression",
        "ADDED_IntersectionType",
        "ADDED_Javadoc",
        "ADDED_LambdaExpression",
        "ADDED_MarkerAnnotation",
        "ADDED_MemberRef",
        "ADDED_MemberValuePair",
        "ADDED_MethodRef",
        "ADDED_MethodRefParameter",
        "ADDED_Modifier",
        "ADDED_ModuleQualifiedName",
        "ADDED_NameQualifiedType",
        "ADDED_NormalAnnotation",
        "ADDED_NullLiteral",
        "ADDED_PackageDeclaration",
        "ADDED_ParameterizedType",
        "ADDED_ParenthesizedExpression",
        "ADDED_PatternInstanceofExpression",
        "ADDED_PostfixExpression",
        "ADDED_PrefixExpression",
        "ADDED_QualifiedType",
        "ADDED_RecordDeclaration",
        "ADDED_SingleMemberAnnotation",
        "ADDED_SuperFieldAccess",
        "ADDED_SuperMethodInvocation",
        "ADDED_SuperMethodReference",
        "ADDED_SwitchExpression",
        "ADDED_TagElement",
        "ADDED_TextBlock",
        "ADDED_TextElement",
        "ADDED_ThisExpression",
        "ADDED_TypeDeclarationStatement",
        "ADDED_TypeLiteral",
        "ADDED_TypeMethodReference",
        "ADDED_TypeParameter",
        "ADDED_UnionType",
        "ADDED_VariableDeclarationExpression",
        "ADDED_WildcardType",
        "CHANGED_AnnotationTypeDeclaration",
        "CHANGED_AnnotationTypeMemberDeclaration",
        "CHANGED_AnonymousClassDeclaration",
        "CHANGED_ArrayAccess",
        "CHANGED_ArrayCreation",
        "CHANGED_ArrayInitializer",
        "CHANGED_ArrayType",
        "CHANGED_BooleanLiteral",
        "CHANGED_CastExpression",
        "CHANGED_CatchClause",
        "CHANGED_CharacterLiteral",
        "CHANGED_ClassInstanceCreation",
        "CHANGED_ConditionalExpression",
        "CHANGED_CreationReference",
        "CHANGED_Dimension",
        "CHANGED_EnumConstantDeclaration",
        "CHANGED_EnumDeclaration",
        "CHANGED_ExpressionMethodReference",
        "CHANGED_FieldAccess",
        "CHANGED_FieldDeclaration",
        "CHANGED_ImportDeclaration",
        "CHANGED_InfixExpression",
        "CHANGED_Initializer",
        "CHANGED_InstanceofExpression",
        "CHANGED_Javadoc",
        "CHANGED_LambdaExpression",
        "CHANGED_MarkerAnnotation",
        "CHANGED_MemberRef",
        "CHANGED_MemberValuePair",
        "CHANGED_MethodRef",
        "CHANGED_MethodRefParameter",
        "CHANGED_Modifier",
        "CHANGED_NormalAnnotation",
        "CHANGED_PackageDeclaration",
        "CHANGED_ParameterizedType",
        "CHANGED_ParenthesizedExpression",
        "CHANGED_PatternInstanceofExpression",
        "CHANGED_PostfixExpression",
        "CHANGED_PrefixExpression",
        "CHANGED_QualifiedType",
        "CHANGED_RecordDeclaration",
        "CHANGED_SingleMemberAnnotation",
        "CHANGED_SuperFieldAccess",
        "CHANGED_SuperMethodInvocation",
        "CHANGED_SwitchExpression",
        "CHANGED_TagElement",
        "CHANGED_TextBlock",
        "CHANGED_TextElement",
        "CHANGED_ThisExpression",
        "CHANGED_TypeLiteral",
        "CHANGED_TypeParameter",
        "CHANGED_UnionType",
        "CHANGED_VariableDeclarationExpression",
        "CHANGED_WildcardType",
        "REMOVED_AnnotationTypeDeclaration",
        "REMOVED_AnnotationTypeMemberDeclaration",
        "REMOVED_AnonymousClassDeclaration",
        "REMOVED_ArrayAccess",
        "REMOVED_ArrayCreation",
        "REMOVED_ArrayInitializer",
        "REMOVED_ArrayType",
        "REMOVED_BooleanLiteral",
        "REMOVED_CastExpression",
        "REMOVED_CatchClause",
        "REMOVED_CharacterLiteral",
        "REMOVED_ClassInstanceCreation",
        "REMOVED_ConditionalExpression",
        "REMOVED_CreationReference",
        "REMOVED_Dimension",
        "REMOVED_EnumConstantDeclaration",
        "REMOVED_EnumDeclaration",
        "REMOVED_ExpressionMethodReference",
        "REMOVED_FieldAccess",
        "REMOVED_FieldDeclaration",
        "REMOVED_ImportDeclaration",
        "REMOVED_InfixExpression",
        "REMOVED_Initializer",
        "REMOVED_InstanceofExpression",
        "REMOVED_IntersectionType",
        "REMOVED_Javadoc",
        "REMOVED_LambdaExpression",
        "REMOVED_MarkerAnnotation",
        "REMOVED_MemberRef",
        "REMOVED_MemberValuePair",
        "REMOVED_MethodRef",
        "REMOVED_MethodRefParameter",
        "REMOVED_Modifier",
        "REMOVED_NameQualifiedType",
        "REMOVED_NormalAnnotation",
        "REMOVED_NullLiteral",
        "REMOVED_PackageDeclaration",
        "REMOVED_ParameterizedType",
        "REMOVED_ParenthesizedExpression",
        "REMOVED_PatternInstanceofExpression",
        "REMOVED_PostfixExpression",
        "REMOVED_PrefixExpression",
        "REMOVED_QualifiedType",
        "REMOVED_RecordDeclaration",
        "REMOVED_SingleMemberAnnotation",
        "REMOVED_SuperFieldAccess",
        "REMOVED_SuperMethodInvocation",
        "REMOVED_SuperMethodReference",
        "REMOVED_SwitchExpression",
        "REMOVED_TagElement",
        "REMOVED_TextBlock",
        "REMOVED_TextElement",
        "REMOVED_ThisExpression",
        "REMOVED_TypeDeclarationStatement",
        "REMOVED_TypeLiteral",
        "REMOVED_TypeMethodReference",
        "REMOVED_TypeParameter",
        "REMOVED_UnionType",
        "REMOVED_VariableDeclarationExpression",
        "REMOVED_WildcardType
        
Includes all other AST nodes as well;


