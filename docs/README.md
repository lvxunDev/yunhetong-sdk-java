# yunhetong-sdk-java
欢迎使用 yunhetong java SDK
我们为您编写了一份详细的[Demo](https://github.com/lvxunDev/yunhetong-java-sdk-demo)
你可以查看详细的[文档](https://github.com/lvxunDev/yunhetong-sdk-java/wiki)
遇到问题可以先去看看我们的 [Issue](https://github.com/lvxunDev/yunhetong-sdk-java/issues)
或者也许你想看看 [JavaDoc](https://lvxundev.github.io/yunhetong-sdk-java/javaDoc.html)

# 快速上手

快速上手要求您有一定的 Java 基础，如果没有就假装自己有。。。

# 0x00 添加依赖
首先添加依赖

**Maven**
```maven
<dependency>
    <groupId>com.yunhetong</groupId>
    <artifactId>sdk</artifactId>
    <version>0.0.4-RELEASE</version>
</dependency>
```

**Gradle**
```gradle
compile 'com.yunhetong:sdk:0.0.4-RELEASE'
```

**jar 包**
或者您可以直接下载 [jar 包](https://search.maven.org/remotecontent?filepath=com/yunhetong/sdk/0.0.4-RELEASE/sdk-0.0.4-RELEASE.jar)


# 0x01 初始化 LxSDKManager
```java
        // 第三方应用的appId
        String appId = "";
        // 云合同公钥地址
        File yhtPublicKey = new File("/path/to/yhtPublicKey.pem");
        // 第三方应用的私钥地址
        File appPrivateKey = new File("/path/to/private.pem");

        LxSDKManager lxSDKManager = null;
        try {
            // 初始化 SDKManager
            lxSDKManager = new LxSDKManager(appId, yhtPublicKey, appPrivateKey);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LxKeyException e) {
            e.printStackTrace();
        } catch (LxNonsupportException e) {
            e.printStackTrace();
        }
```


# 0x02 导入用户
我们要导入用户并且获取 token
- 准备用户数据

```java
    private LxUser getUserA() {
        LxUser lxUser = new LxUser();
        lxUser.setAppUserId("your app Id")                    // 设置 appID
                .setCertifyNumber("123")                      // 设置证件号码
                .setUserType(LxUser.UserType.USER)            // 设置用户类型
                .setPhone("12311111111")                      // 设置手机号码
                .setUserName("userA")                          // 设置用户名
                .setCertifyType(LxUser.CertifyType.ID_CARD)   // 设置实名认证类型
        ;
        return lxUser;
    }
```

- 导入用户

```java
try {
        String s = lxSDKManager.syncGetToken(getUserA());
    } catch (IOException e) {
        e.printStackTrace();
    } catch (LxEncryptException e) {
        e.printStackTrace();
    } catch (LxKeyException e) {
        e.printStackTrace();
    } catch (LxNonsupportException e) {
        e.printStackTrace();
    } catch (LxDecryptException e) {
        e.printStackTrace();
    } catch (LxSignatureException e) {
        e.printStackTrace();
    } catch (LxVerifyException e) {
        e.printStackTrace();
    }
```

- 返回结果
正常会返回如下所示字符串

```json
{"code":200,"message":"true","subCode":200,"value":{"contractList":[{"id":1701061349385004,"status":"签署中","title":"测试合同标题40"},{"id":1701031046255028,"status":"签署中","title":"测试合同标题25"}],"token":"TGT-31356-4FZDJcQR3yK4IiaWIafnxQY0QAIoAI0SP6jja0VFY65PJ1S2W4-cas01.example.org"}}
```

然后将 token 返回给客户端，客户端再通过这个 token 去调用相应的SDK（比如js SDK 或 Android SDK 或 iOS SDK），去访问合同操作

# 0x03 生成合同
初始化 LxSDKManager 略，参考上面第一条。假设有个 A,B 两个人，A 要发起一份合同合同给 B，此时 A是合同的发起方， 也是合同的参与方。以此为例，代码如下
- 准备用户 B 信息
参考上面第二条用户 A 的信息，用户 B 的代码如下

```java
/**
     * 生成用户 B
     * @return 返回用户 B
     */
    private LxUser getUserB() {
        LxUser lxUser = new LxUser();
        lxUser.setAppUserId("your App Id")            // 设置 appID
                .setCertifyNumber("123")                      // 设置证件号码
                .setUserType(LxUser.UserType.USER)            // 设置用户类型
                .setPhone("12311111111")                      // 设置手机号码
                .setUserName("userB")                          // 设置用户名
                .setCertifyType(LxUser.CertifyType.ID_CARD)   // 设置实名认证类型
        ;
        return lxUser;
    }
```

- 准备合同信息

```java
    /**
     * 创建测试合同
     * @return 测试合同
     */
    private LxContract getTestContract() {
        LxContract lxContract = new LxContract();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("${nameA}", "nameA");
        lxContract.setDefContractNo("contractNo")           // 设置自定义合同编号
                .setTemplateId("templateId")                // 设置合同模板 Id
                .setTitle("title")                          // 设置合同标题
                .setParams(params)                          // 这是模板占位符
        ;
        return lxContract;
    }
```
- 准备合同参与方

在刚才的用户A、B的基础上，我们可以生成合同的参与方

```java
    /**
     * 创建合同参与方
     * @return 返回合同参与方
     */
    private LxContractActor[] getActor(){
        LxContractActor actorA = new LxContractActor();
        actorA.setUser(getUserA());
        actorA.setAutoSign(false);
        actorA.setLocationName("signA");

        LxContractActor actorB = new LxContractActor();
        actorA.setUser(getUserB());
        actorA.setAutoSign(false);
        actorA.setLocationName("signB");
        return new LxContractActor[]{actorA, actorB};
    }
```

- 生成合同

```java
String s = lxSDKManager.createContract(getTestContract(),getActor());
```

- 返回结果
正常的话会返回如下所示字符串

```
{"code":200,"message":"true","subCode":200,"value":{"contractId":1701061352090008}}
```
将上一步得到的 token 和这里的 contractId 返回给客户端，即可用相应的 SDK（比如js SDK 或 Android SDK 或 iOS SDK），去进行合同的相关操作。

# 0x04 通过创建合同获取 token
有时候我们想在创建合同的同时也获取 Token，我们可以像下面这样
```java
String s = lxSDKManager.getTokenWithContract(getUserA(),getTestContract(),getActor());
```

正常的话会返回如下所示字符串
```json
{"code":200,"message":"true","subCode":200,"value":{"contractId":1701061349385004,"token":"TGT-31353-vpnotTbYFJ5wXoTUDzjSD9eVqZfzx9RZIsUhqGcEL5kjRcS6V6-cas01.example.org"}}

```


# 0x05 End
就是这么简单方便