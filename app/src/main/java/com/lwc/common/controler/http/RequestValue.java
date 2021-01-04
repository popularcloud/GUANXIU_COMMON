package com.lwc.common.controler.http;

/**
 * 网络请求接口
 *
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 */
public class RequestValue {

    /**
     * 用户登录
     */
    public static final String METHOD_NEW_USER_LOGIN ="/user/login_2_9";

    /**
     * 用户验证码登录
     */
    public static final String METHOD_NEW_USER_CODE_LOGIN ="/user/loginByCode";

    /**
     * 订单评价
     */
    public static final String METHOD_ORDER_STATUS_SET = "/order/comment_2_8";

    // ----------------------更新相关--------------------------------------------------------------------------------------------------
    /**
     * 意见反馈
     */
    public static final String METHOD_SUGGEST = "/feedback/insert";
    /**
     * 我的订单
     */
    public static final String MY_ORDERS = "/new/order/userOrder";

    /**
     * 下单状态
     */
    public static final String ORDER_STATE = "/order/process/";

    /**
     * 用户申请售后订单
     */
    public static final String ORDER_WARRANTY_SAVE = "/after/sale/apply";

    /**
     * 用户确定完成售后订单
     */
    public static final String ORDER_AFTER_FINISH = "/after/sale/confirmFinish";

    /**
     * 用户取消售后订单
     */
    public static final String ORDER_AFTER_CANCEL = "/after/sale/cancel";

    /**
     * 取消订单
     */
    public static final String ORDER_CANCEL = "/order/cancel";

    /**
     * 确认完成订单
     */
    public static final String ORDER_FINISH ="/order/confirmFinish";

    /**
     * 确认报价订单
     */
    public static final String ORDER_REPLACEMENT ="/order/confirmReplacement";

    /**
     * 找回密码
     */
    public static final String BACK_PWD = "/user/retrievePass";
    /**
     * 版本升级（更新）
     **/
    public static final String UPDATE_APP = "/version/check_2_9";

    /**
     * 投诉维修员接口
     */
    public static final String COMPLAINT = "/fb/save.phone";

    /**
     * 首页订单视图接口
     */
    public static final String ORDER_VIEW = "/inform/title";

    /**
     * 退出APP
     **/
    public static final String EXIT = "/user/exit";

    /**
     * 附近工程师
     */
    public static final String NEAR_ENGINEER = "/near/maintenance";

    /**
     * 更新用户信息
     */
    public static final String UP_USER_INFOR = "/user/modify";

    /**
     * 上传微信信息
     */
    public static final String UP_UPLOAD_INFO = "/newYear/activity/shareCall";

    /**
     * 完善用户信息
     */
    public static final String IMPROVE_USER_INFOR = "/user/authOrgUser";
    /**
     * 用户信息
     */
    public static final String USER_INFO = "/user/info";

    /**
     * 上传图片
     */
    public static final String UP_PICTURE = "/file/upload.phone";

    /**
     * 维修类型
     */
    public static final String MALFUNCTION = "/repair/getAll";

    /**
     * 维修公司
     */
    public static final String REPAIRS_COMPANY = "/company/server";

    /**
     * 获取标签
     */
    public static final String GET_LABEL = "/order/comment/label";

    /**
     * 我的技能
     */
    public static final String METHOD_GET_MY_SKILLS = "/repair/getSkill";

    /**
     * 故障类型
     */
    public static final String GET_SKILLS_TYPE = "/type/getRepairs";

    /**
     * 用户地址
     */
    public static final String GET_USER_ADDRESS = "/user/address/list";

    /**
     * 添加或修改用户地址
     */
    public static final String ADD_OR_AMEND_ADDRESS = "/user/address/save";

    /**
     * 删除用户地址
     */
    public static final String DELETE_ADDRESS = "/user/address/del/";

    /**
     * 新的提交订单接口
     */
    public static final String SUBMIT_ORDER = "/new/order/save";

    /**
     * 地理位置
     */
    public static final String JUDGEREGION = "/package/judgeRegion";

    /**
     * 注册2
     */
    public static final String REGISTER2 = "/user/register_2_9";

    /**
     * 完善资料
     */
    public static final String PERFECT_USER_INFO = "/user/perfectUserInfoByCode";

    /**
     * 获取验证码
     */
    public static final String GET_CODE = "/inform/sendSms/";

    /**
     * 用户催单
     */
    public static final String ORDER_PRESS = "/order/press";

    /**
     * 用户拒绝
     */
    public static final String ORDER_REFUSE = "/order/refuse";

    /**
     * 获取上传图片token
     */
    public static final String GET_PICTURE = "/oss/getAuth";

    /**
     * 活动广播
     */
    public static final String GET_ANNUNCIATE = "/inform/annunciate";

    /**
     * 收支记录
     */
    public static final String GET_WALLET_HISTORY = "/user/payment/record";

    /**
     * 拆红包
     */
    public static final String GET_RED_PACKET_MONEY = "/packet/open";

    /**
     * 申请提现
     */
    public static final String POST_WITHDRAW_DEPOSIT = "/packet/withdraw";

    /**
     * 检查是否有红包活动
     */
    public static final String GET_CHECK_ACTIVITY = "/user/check/activity";

    /**
     * 分享统计
     */
    public static final String GET_INFORMATION_SHARE = "/information/updateShare/";

    /**
     * 启动图片
     */
    public static final String GET_USER_MODIFY = "/user/modify";

    /**
     * 查询待开发票订单列表(订单)
     */
    public static final String GET_INVOICE_ORDER_LIST = "/user/invoice/order";

    /**
     * 查询待开发票订单列表(套餐)
     */
    public static final String GET_INVOICE_PACKAGE_LIST = "/user/invoice/package";

    /**
     * 查询待开发票订单列表(租赁)
     */
    public static final String GET_INVOICE_LEASEORDER_LIST = "/user/invoice/leaseOrder";

    /**
     * 查询用户开发票历史列表
     */
    public static final String GET_INVOICE_HISTORY_LIST = "/user/invoice/history";

    /**
     * 提交开票
     */
    public static final String POST_INVOICE = "/user/invoice/save";

    /**
     * 查询用户发票信息
     */
    public static final String GET_INVOICE_INFO = "/user/invoice/info";

    /**
     * 查询用户认证信息
     */
    public static final String GET_USER_AUTH_INFO = "/user/checkAuth";

    /**
     * 撤销
     */
    public static final String GET_USER_REPEAL_AUTH = "/user/repealAuth";

    /**
     * 查询订单详情
     */
    public static final String POST_ORDER_INFO = "/new/order/personal/info";

    /**
     * 查询维修师资料
     */
    public static final String POST_MAINTENANCE_INFO = "/user/maintenanceComment";

    /**
     * 查询费用
     */
    public static final String GET_PRICE_MSG = "/type/priceMsg";

    /**
     * 查询报修提示
     */
    public static final String GET_PRICE_MSG_2_9 = "/type/priceMsg_2_9";

    /**
     * 查询报价单
     */
    public static final String GET_DETECTION_INFO = "/new/order/personal/detectionInfo";

    /**
     * 查询订单可用优惠券列表
     */
    public static final String GET_USER_COUPON_LIST = "/user/coupon/usableList";

    /**
     * 拒绝返厂维修
     */
    public static final String POST_DETECTION_REFUSE = "/order/personal/countermand/";

    /**
     * 同意返厂维修
     */
    public static final String POST_DETECTION_TONGYI = "/order/personal/countersign/";

    /**
     * 订单支付
     */
    public static final String POST_PAY_INFO = "/pay/info";

    /**
     * 订单申诉
     */
    public static final String POST_ORDER_APPEAL = "/order/personal/appeal/";

    /**
     * 查询我的消息列表
     */
    public static final String GET_MY_MESSAGE_LIST = "/message/list";

    /**
     * 查询某类型消息列表
     */
    public static final String GET_MESSAGE_LIST = "/message/detailsList/";

    /**
     * 检查是否有新消息
     */
    public static final String HAS_MESSAGE = "/message/hasMessage";

    /**
     * 新消息阅读回调
     */
    public static final String READ_MESSAGE = "/message/readOne/";

    /**
     * 解除绑定
     */
    public static final String RELIEVE_BIND = "/user/relieveBind";

    /**
     * 绑定公司
     */
    public static final String BIND_COMPANY = "/user/bindCompany";

    /**
     * 启动广告图
     */
    public static final String INFORM_BOOT_PAGE = "/inform/bootPage";

    /**
     * 查询所有设备类型
     */
    public static final String GET_TYPE_ALL = "/type/getAll";

    /**
     * 查询工程师评价信息
     */
    public static final String GET_COMMENT_LIST = "/order/comment/list";

    /**
     * 查询优惠券列表
     */
    public static final String GET_COUPON_LIST = "/user/coupon/index";

    /**
     * 查询轮播广告图
     */
    public static final String GET_ADVERTISING = "/information/advertising";


    /**
     * 邀请维修
     */
    public static final String INFORM_INVITE = "/inform/invite";

    /**
     * 扫设备二维码
     */
    public static final String SCAN_CODE = "/scan/code";

    /**
     * 扫租赁设备二维码
     */
    public static final String SCAN_LEASECODE = "/scan/leaseCode";

    /**
     * 用户优惠券领取
     */
    public static final String USER_COUPON_GET = "/user/coupon/get";

    /**
     * 可购买的套餐列表
     */
    public static final String GET_PACKAGE_LIST = "/package/list";

    /**
     * 用户的套餐列表
     */
    public static final String GET_USER_PACKAGE_LIST = "/package/userPackage";

    /**
     * 用户选择套餐列表
     */
    public static final String GET_USER_SELECT_PACKAGE_LIST = "/package/selectList";

    /**
     * 购买套餐
     */
    public static final String POST_PAY_BUY_PACKAGE = "/pay/buyPackage";

    /**
     * 拒绝返厂(拒绝维修)
     */
    public static final String GET_REFUSED = "/new/order/refused/returnFactory";

    /**
     * 获取配件库类型
     */
    public static final String GET_ACCESSORIES_TYPES = "/accessories/getAccessoriesTypes";

    /**
     * 搜索配件库
     */
    public static final String GET_ACCESSORIES_ALL = "/accessories/getAccessoriesAll";

    /**
     * 获取配件详情
     */
    public static final String GET_ACCESSORIES = "/accessories/getAccessories";

    /**
     * 获取配件筛选
     */
    public static final String GET_ACCESSORIESTYPEALL = "/accessories/getAccessoriesTypeAll";

    /**
     * 更加上一级获取维修类型
     */
    public static final String GET_TYPE_ALL_BY_UPER = "/type/getAll";

    /**
     * 获取所有的维修类型
     */
    public static final String GET_TYPE_ALL__2 = "/type/getAll_2";

    /**
     * 分享订单
     */
    public static final String GET_ORDER_ORDERSHARE = "/order/orderShare";

    /**
     * 充值
     */
    public static final String GET_WEBAPPPAY_APPPAY = "/main/webAppPay/appPay";

    public static final String GET_PAY_RECHARGE = "/pay/recharge";


    /**
     * 积分商品列表
     */
    public static final String GET_INTEGRAL_GOODS = "/integral/goods";

    /**
     * 积分商品详情
     */
    public static final String GET_INTEGRAL_GOODSDETAIL = "/integral/goodsDetail";

    /**
     * 用户积分记录
     */
    public static final String GET_INTEGRAL_RECORD= "/integral/record";

    /**
     * 兑换商品
     */
    public static final String INTEGRAL_EXCHANGE= "/integral/exchange";

    /**
     * 积分兑换列表
     */
    public static final String GET_INTEGRAL_EXCHANGE_GOODS= "/integral/exchange/goods";

    /**
     * 积分兑换详情
     */
    public static final String GET_INTEGRAL_EXCHANGE_GOODSDETAIL= "/integral/exchange/goodsDetail";

    /**
     * 第三方登录(第一次提交信息)
     */
    public static final String GET_USER_LOGINBYTHIRD= "/user/loginByThird";


    /**
     * 第三方登录(绑定手机)
     */
    public static final String GET_USER_BINDBYTHIRD= "/user/bindByThird";


    /**
     * 签到记录
     */
    public static final String GET_SIGN_SIGNTIME = "/user/sign/signTime";

    /**
     * 签到
     */
    public static final String GET_SIGN_SIGNIN = "/user/sign/signin";

    /**
     * 收费标准
     */
    public static final String GET_INFORMATION_CHARGINGSTANDARD = "/information/chargingStandard";

    /**
     * 获取积分抽奖数据
     */
    public static final String GET_LUCKY_LUCKYROOM = "/lucky/luckyRoom";

    /**
     * 积分抽奖
     */
    public static final String GET_LUCKY_LUCKYDRAW = "/lucky/luckyDraw";

    /**
     * 生成邀请二维码链接
     */
    public static final String GET_USER_ADDASSOCIATIONINFO= "/user/addAssociationInfo";

    /**
     * 用户收益记录
     */
    public static final String GET_PAYMENT_INVITERECORD= "/user/payment/inviteRecord";

    /**
     * 绑定邀请二维码
     */
    public static final String GET_USER_BINDASSOCIATIONINFO= "/user/bindAssociationInfo";

    /**
     * 获取租赁设备类型
     */
    public static final String SCAN_GETLEASEDEVICETYPES = "/scan/getLeaseDeviceTypes";

    /**
     * 获取租赁广告图
     */
    public static final String LEASEMANAGE_GETLEASEIMAGES = "/leaseManage/getLeaseImages";

    /**
     * 获取租赁商品推荐栏目
     */
    public static final String LEASEMANAGE_GETLEASERECOMMENDS = "/leaseManage/getLeaseRecommends";

    /**
     * 获取租赁商品
     */
    public static final String LEASEMANAGE_GETLEASEGOODS = "/leaseManage/getLeaseGoods";

    /**
     * 获取租赁商品详情
     */
    public static final String LEASEMANAGE_GETLEASEGOOD = "/leaseManage/getLeaseGood";

    /**
     * 获取租赁商品相关规格
     */
    public static final String LEASEMANAGE_GETLEASESPECSREVELENCE = "/leaseManage/getLeaseSpecsRevelence";

    /**
     * 加入购物车
     */
    public static final String LEASEMANAGE_ADDLEASEGOODSCAR = "/leaseManage/addLeaseGoodsCar";

    /**
     * 查看购物车
     */
    public static final String LEASEMANAGE_QUERYLEASEGOODSCAR = "/leaseManage/queryLeaseGoodsCar";

    /**
     * 确认订单
     */
    public static final String LEASEMANAGE_ORDER_SAVE = "/leaseManage/order/save";

    /**
     * 查询商品优惠价格
     */
    public static final String LEASEMANAGE_GOODSACTIVITY = "/leaseManage/goodsActivity";

    /**
     * 拉取支付信息
     */
    public static final String LEASEMANAGE_PAY_LEASEINFO = "/pay/leaseInfo";

    /**
     * 拉取租赁商品续费支付信息
     */
    public static final String LEASEMANAGE_PAY_LEASERENEWALINFO = "/pay/leaseRenewalInfo";


    /**
     * 查看我的租赁订单
     */
    public static final String LEASEMANAGE_QUERYLEASEORDERS = "/leaseManage/queryLeaseOrders";

    /**
     * 查看我的租赁订单(退款、退租)
     */
    public static final String LEASEMANAGE_QUERYLEASEBRANCHORDERS = "/leaseManage/queryLeaseBranchOrders";

    /**
     * 查看我的租赁商品类型
     */
    public static final String LEASEMANAGE_GETDEVICETYPES = "/leaseManage/getDeviceTypes";

    /**
     * 查看我的租赁商品类型小类
     */
    public static final String LEASEMANAGE_GETDEVICETYPEDETAILS = "/leaseManage/getDeviceTypeDetails";

    /**
     * 租赁商品筛选内容
     */
    public static final String LEASEMANAGE_GETLEASETYPEALL = "/leaseManage/getLeaseTypeAll";

    /**
     * 租赁商品搜索历史及热搜榜
     */
    public static final String LEASEMANAGE_LEASEGOODSKEYWORD = "/leaseManage/leaseGoodsKeyword";

    /**
     * 加入收藏夹
     */
    public static final String LEASEMANAGE_ADDLEASEGOODSCOLLECTION = "/leaseManage/addLeaseGoodsCollection";

    /**
     * 查看我的租赁订单详情
     */
    public static final String LEASEMANAGE_GETLEASEORDER = "/leaseManage/getLeaseOrder";

    /**
     * 查看我的租赁订单详情（退款，退租）
     */
    public static final String LEASEMANAGE_GETLEASEBRANCHORDER = "/leaseManage/getLeaseBranchOrder";

    /**
     * 用户申请认证
     */
    public static final String LEASEMANAGE_AUTHORGUSER = "/leaseManage/authOrgUser";

    /**
     * 删除购物车
     */
    public static final String LEASEMANAGE_DELLEASEGOODSCAR = "/leaseManage/delLeaseGoodsCar";

    /**
     * 购物车移入收藏夹
     */
    public static final String LEASEMANAGE_LEASEGOODSCARTOCOLLE = "/leaseManage/leaseGoodsCarToColle";

    /**
     * 取消租赁订单
     */
    public static final String LEASEMANAGE_CANCELLEASEORDER = "/leaseManage/cancelLeaseOrder";

    /**
     * 用户申请退款
     */
    public static final String LEASEMANAGE_APPLYREFUND = "/leaseManage/applyRefund";

    /**
     * 用户申请退租
     */
    public static final String LEASEMANAGE_APPLYREFUNDGOODS = "/leaseManage/applyRefundGoods";

    /**
     * 用户确认收货
     */
    public static final String LEASEMANAGE_INLEASE = "/leaseManage/inLease";

    /**
     * 用户缴费详情
     */
    public static final String LEASEMANAGE_USERPAYDETAIL = "/leaseManage/userPayDetail";

    /**
     * 我的收藏
     */
    public static final String LEASEMANAGE_QUERYLEASEGOODSCOLLECTION = "/leaseManage/queryLeaseGoodsCollection";

    /**
     * 商品名称列表
     */
    public static final String LEASEMANAGE_GOODSLEASEINFO = "/leaseManage/goodsLeaseInfo";

    /**
     * 修改购物车
     */
    public static final String LEASEMANAGE_MODLEASEGOODSCAR = "/leaseManage/modLeaseGoodsCar";


    /**
     * 删除收藏夹
     */
    public static final String LEASEMANAGE_DELLEASEGOODSCOLLECTION = "/leaseManage/delLeaseGoodsCollection";

    /**
     * 用户撤销退款，退租申请
     */
    public static final String LEASEMANAGE_UODOLEASEBRANCHORDER = "/leaseManage/uodoLeaseBranchOrder";

    /**
     * 查询用户订单数量
     */
    public static final String LEASEMANAGE_ORDERNUMDATA = "/leaseManage/orderNumData";

    /**
     * 删除租赁订单(退租,退款)
     */
    public static final String LEASEMANAGE_DELETELEASEBRANCHORDER = "/leaseManage/deleteLeaseBranchOrder";

    /**
     * 删除租赁订单
     */
    public static final String LEASEMANAGE_DELETELEASEORDER = "/leaseManage/deleteLeaseOrder";

    /**
     * 查看我的租赁认证信息
     */
    public static final String LEASEMANAGE_GETAUTHORGUSER = "/leaseManage/getAuthOrgUser";

    /**
     * 清空租赁商品搜索历史
     */
    public static final String LEASEMANAGE_DELLEASEGOODSKEYWORD = "/leaseManage/delLeaseGoodsKeyword";

}
