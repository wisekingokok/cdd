/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 * 
 *  提示：如何获取安全校验码和合作身份者id
 *  1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *  2.点击“商家服务”(https://b.alipay.com/order/myorder.htm)
 *  3.点击“查询合作者身份(pid)”、“查询安全校验码(key)”
 */

package com.chewuwuyou.app.alipay;

//
// 请参考 Android平台安全支付服务(msp)应用开发接口(4.2 RSA算法签名)部分，并使用压缩包中的openssl RSA密钥生成工具，生成一套RSA公私钥。
// 这里签名时，只需要使用生成的RSA私钥。
// Note: 为安全起见，使用RSA私钥进行签名的操作过程，应该尽量放到商家服务器端去进行。
public final class Keys {

	// 合作身份者id，以2088开头的16位纯数字
	public static final String DEFAULT_PARTNER = "2088611939495014";

	// 收款支付宝账号
	public static final String DEFAULT_SELLER = "chewuwuyou@163.com";

	// 商户私钥，自助生成
	// public static final String PRIVATE =
	// "MIICXAIBAAKBgQC7f+qhlbhogAAxFdVWMcQud4EMgSfWlKzIXuecoeLrbRQY8Do6G6X3bOOiypFdWs3LIOjNJYQRUJ5XlRHvVKAdYE+OGSbsqGTUHlahzGBpSOS9vwOm+SGd8VXJYWTAqiF3++QnaBCWa0A+QKydnG3tib2D7clmxTSip883zQXjfwIDAQABAoGAOwiJPRrAltZGOS4gnyTIGcEydldrzZm91NBcp+ooYetT+o4YJxdxZ7d27FhO8E+Yn63a3kxDipgRYo3iubGxP0Qv9ij9ZMKzEVebuxJuPq2mnOpSwYmDulOfdE5Ek+LnWlH3buWsJf+XrBfmw1sryI42Lo13S+m4idHfbip2OXECQQDr0+8vmZSuhyApJE78V4RZVlXZVWNT4CLT4Xd3KxVJInzEvf/oYlln0zC0OG1mIMefY1V1vBkR1dOlQICC97qLAkEAy4m14LMDddxczgC841LAA8NgHnU4TWzoB3jvqEKgC6wQg4cdSWaUznQUOo8YZF2gqt7TFeRYIm35rxguizw9XQJBAIhccLaeYImWWrNPalihJkceE9/qbtfGB31qptCCWfmC9pAwqPOvQrE1PblH04I1PBhesMC2NezJLmskY+rW4ccCQFIiGgC4fDb9W33NkmtxIwbhulHQH44u2lrZlGwjpo3PO0K4LTtHZ9j/woH26HLnzUPmcuusn1wVYOIlMfMBtgkCQGL+eRty5ZHYompF9IkZ8Zzq8R8/LjH4boXHUosjD9niC4PQYb4imSRjIvOapuEu5ncBXk76d9IIAWEk8hwRWS0=";
	// public static final String PRIVATE =
	// "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALMm+mbPPQVWcVdvK4w+0PgEbLTddQVVmD7lMOxt92StPJfr4/5NXq3sA0dQhozySH/1XyYXGk7e4yJ8QHwuDvND2oiUcVdrEL4U6xX6XU1jdjH5tnIv6Wq2boV2vdFz+ZG7ivp/HQvPOqUgOrzUpf1ONlreq167B5t1hp97dYiLAgMBAAECgYARJHarZcMiAp5/zkxtdAlM4ieAuvwakZEq24Vt6ivU/1qf8ttzFvYrUD0UEtl9HSXg0JrVKzrCN0OmphsJhuK5LU85/sd3+q2dMmALXlvdSnKKcgOYTdopdcOi35JdrOxbxRiPITHJSJnWg+e1LERqmlXXMmUFe9n/xf67s1qJYQJBAN36vre/tSckR9e7bjLyAmH2U6YTQ5JmbquvIlub/tiv+IjmOO3dywvRnIY7ogusHlRvYGs4AAvLr/59m4ro51sCQQDOm+20TRjZg+D9vNTKRGczowV0Ah9+GZE2Ld5XdWB+tcqdoHXkUzIiCFxCDhVbTUcAng1XpDLteBui27bnRdqRAkEA3e6bDXVS9Z5VYcAX7kF91lmDylAKR+23iljgHf0574UIFD8w/ci4/8kDGEW4vrjH3h04phnoGeL8w5DrmRdAbwJASzN6K7w8JT/ZLsICebS4E5VssLLtH4rSzfOYECFLBBPLMeBryLV3c+ezHN/faiIQi+r7VEERwFmNRZPPWRKV4QJAKt5jBt0uPWB02GsTxRL29AFK9EycNgG6RVLkOyTqU3D9Ljt5SsBB0lyXdleB9O5puyCRYLgkmU9hBRsTDjD1+g==";
	public static final String PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
	// 最新私钥
	public static final String PRIVATE = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBANoxbs44pT/KtuOQQZRZzt2KpzH+Xs/C0D4gLAICnBfMTOmJQvoLC+hKhVHYW/mYDPLfAJtecA3ZFMa2ZCCm2W/vsYN5BiSkn+i2w51NT7cU4EIV5YNsbdudjH0m4ZqoevLOa9/f5KejLOgZKP782txXkkuuImw/pu+VbyRuMwHFAgMBAAECgYEApZ+OX6Vp8oFa0GRJE2YUe+Bqx60xpP1Y+z+KiEAY4oum+kLB/3FxKXOfvZ/BOTXs5lLWcBmAb1KPzcLXQF3GgCWVqK7S00fkoIXfwESiUq4HTQjqcljvtnuLaMc5lF/+2x18KG0uCjDvgl3ubkORPaKEbV0EEgGyxwJT3QI7byECQQDwH21/A82fycBFaLIcvL4ylLZVcFcHQxqA8HosUYj9WQmw5jj6Q9jg0kUCWaP/u2g9JJUc6cRn2XfDMMga20B9AkEA6J7M4opj7odpQSvfdb5+68jUv0rPU7ywTwFQnSEjTl+5aOet5iU/1U722/Stu1/djp7jqjVPgy1XG3uQ3wuQ6QJBALfDpX2eAIB32zCDRkQd4gU9sCjE16WUIjVpVGaUY51XxcXe+t30jIbKBMwepBazarvYQlGlSY0cbb4wmAONpdUCQCzZ0W8sd7AZ87VZHDC7lgUoJ7zqU6c1jljtS/m/1erakgkkAmeJbxFVtEsclhK2nSVOhV7gAICuUUk/PeODe2ECQQCIHQEKeiXhv6Exu5/UC5X4fqyOtc9UjSMMlmEzW4t8QzpVFMnGKQsLaIWgKX0cfW/AFfwQu5njkH9fPJM66ZZ3";
}
