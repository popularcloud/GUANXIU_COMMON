package com.lwc.common.configs;

/**
 * @Description 数据库字段类
 * @author 何栋
 * @date 2015年8月26日
 * @Copyright: Copyright (c) 2015 Shenzhen Tentinet Technology Co., Ltd. Inc. All rights reserved.
 */
public class DataBaseFields {

	// *******************************公用字段*****************************//
	/** 本地数据库自增长Id */
	public static final String ID = "id";
	/** 消息Id */
	public static final String MESSAGE_ID = "message_id";
	/** 消息类型信息类型（1为文本，2为语音，3为图片，4为视频 ， 5其他文件,23为名片） */
	public static final String MESSAGE_TYPE = "message_type";
	/** 消息内容 */
	public static final String MESSAGE = "message";
	/** 是否已读（1未读，2已读） */
	public static final String IS_READ = "is_read";
	/** 发送时间 */
	public static final String SEND_TIME = "send_time";
	/** 用户聊天会话Id */
	public static final String USER_JID = "user_jid";
	/** 用户Id */
	public static final String USER_ID = "user_id";
	/** 用户详情Id */
	public static final String USER_INFO_ID = "user_info_id";
	/** 帐号、用户名 */
	public static final String USERNAME = "username";
	/** 用户昵称 */
	public static final String NICKNAME = "nickname";
	/** 用户备注 */
	public static final String REMARK = "remark";
	/** 真实姓名 */
	public static final String REALNAME = "realname";
	/** 用户头像 */
	public static final String PORTRAIT = "portrait";
	/** 性别 */
	public static final String SEX = "sex";
	/** 个性签名 */
	public static final String SIGNATURE = "signature";
	/** 状态 */
	public static final String STATE = "state";
	/** 是否允许推荐消息 */
	public static final String IS_ALLOW_RECOMMEND = "is_allow_recommend";
	/** 类型 */
	public static final String TYPE = "type";
	/** 权重 */
	public static final String WEIGHT = "weight";
	/** 聊天对象Id */
	public static final String CHAT_OBJECT_ID = "chat_object_id";
	/** 聊天对象账号 */
	public static final String CHAT_OBJECT_USERNAME = "chat_object_username";
	/** 聊天对象昵称 */
	public static final String CHAT_OBJECT_NICK = "chat_object_nick";
	/** 聊天对象头像 */
	public static final String CHAT_OBJECT_PORTRAIT = "chat_object_portrait";
	/** 内容 */
	public static final String CONTENT = "content";
	/** 是否为我发出的信息（1为我发出，2为对方发出） */
	public static final String IS_MINE = "is_mine";
	/** 发送状态 1:成功，0:失败 */
	public static final String SEND_STATUS = "send_status";
	/** 群id */
	public static final String GROUP_ID = "group_id";

	// *************************************推送信息表***********************************//
	/** 推送信息表 */
	public static final String TB_PUSH_MESSAGE = "tb_push_message";
	/** 推送消息标题 */
	public static final String TITLE = "title";
	/** 推送消息链接网页 */
	public static final String PATH = "path";
	/** 推送消息图片地址 */
	public static final String IMAGES = "images";
	/** 消息是否已读（1未读，2已读） */
	/** 推送消息id */
	/** 发送时间 */
	/** 推送消息内容 message */

	// *************************************XMPP用户表***********************************//
	/** 用户表 */
	public static final String TB_USER = "tb_user";
	/** 密码 */
	public static final String PASSWORD = "password";
	/** 生日 */
	public static final String BIRTHDAY = "birthday";
	/** 身份证号码 */
	public static final String ID_CARD_NUMBER = "id_card_number";
	/** 省份 */
	public static final String PROVINCE = "province";
	/** 城市 */
	public static final String CITY = "city";
	/** 手机号码 */
	public static final String PHONE_NUM = "phone_num";
	/** 手机归属地 */
	public static final String SERIAL_NUM = "serial_num";
	/** ticket */
	public static final String TICKET = "ticket";
	/** 是否记住密码 */
	public static final String IS_REMEMBER = "is_remember";
	/** 是否自动登录 */
	public static final String IS_AUTO_LOGIN = "is_auto_login";
	/** 邮箱 */
	public static final String EMAIL = "email";
	/** 最后登录时间 */
	public static final String LAST_LOGIN_TIME = "last_login_time";
	/** 公司名称 */
	public static final String COMPANY = "company";
	/** 详细地址 */
	public static final String ADDRES = "addres";
	/** 是否是维修员 */
	public static final String IS_REPAIRER = "is_repairer";
	/** 绑定设备udid */
	public static final String BINDDID = "bindudid";
	/** 绑定设备名称 */
	public static final String DEVICENAME = "devicename";
	/** 绑定设备did */
	public static final String DID = "did";
	public static final String AVGSERVICE = "avgservice";
	public static final String AVGSPECIALTY = "avgspecialty";
	public static final String AVGREPLY = "avgreply";
	public static final String STAR = "star";
	// *************************************XMPP新好友表***********************************//
	/** XMPP好友表 */
	public static final String TB_NEW_FRIEND = "tb_new_friend";
	/** 用户Id */
	/** 新朋友昵称 */
	/** 新朋友真实姓名 */
	/** 新朋友头像 */
	/** 新朋友性别 */
	/** 新朋友账号 */
	/** 新朋友签名 */
	/** 新朋友类别（1为可发送好友邀请，2为可同意或拒绝好友，3为同意加为好友，4为拒绝加为好友） */
	public static final String FRIEND_TYPE = "friend_type";
	/** 来源（1为通讯录，2为摇一摇） */
	public static final String SOURCE = "source";
	/** 是否已读（0未读，1已读） */
	/** 是否是我的 */
	/** 聊天会话Id */

	// ************************************好友（通讯录）表************************************//
	public static final String TB_FRIEND = "tb_friend";
	/** 用户聊天会话Id */
	/** 好友Id */
	/** 好友详情Id */
	/** 帐号 */
	/** 性别 */
	/** 头像 */
	/** 昵称 */
	/** 备注 */
	/** 真实姓名 */
	/** 签名 */
	/** 是否允许推荐消息 */
	/** 权重，用于标识帐号的优先级别 */
	/** 类型 0系统用户，1其他系统用户，2是公众帐号 */

	// ************************************聊天记录表************************************//
	/** 聊天记录数据表 */
	public static final String TB_CHAT = "tb_chat";
	/** 聊天语音时长 */
	public static final String MESSAGE_TIME = "message_time ";
	/** 消息来源 */
	public static final String MESSAGE_SOURCE = "message_source";
	/** 是否为我发出的信息（1为我发出，2为对方发出） */
	/** 聊天对象Id */
	/** 聊天对象账号 */
	/** 聊天对象昵称 */
	/** 聊天对象头像 */
	/** 消息类型信息类型（1为文本，2为语音，3为图片，4为视频 ， 5其他文件） */
	/** 是否已经阅读 */
	/** 是否发送成功 */
	/** 发送时间 */
	/** 消息内容 */
	/** 13位的时间戳 */
	public static final String TIME_STAMP = "time_stamp";

	// **************************************群成员表*************************************//
	/** 群成员表 */
	public static final String TB_GROUP_MEMBERS = "tb_group_members";
	/** 群id */
	/** 群成员id */
	/** 成员账户 */
	/** 头像 */
	/** 昵称 */
	/** 权限 */
	public static final String AFFILIATION = "affiliation";

	// *********************************群聊天记录表*****************************//

	/** 群聊天记录数据表 */
	public static final String TB_CHAT_GROUP = "tb_chat_group";
	/** 聊天群id */
	public static final String CHAT_GROUP_ID = "chat_group_id";
	/** 聊天群名称 */
	public static final String CHAT_GROUP_NAME = "chat_group_name";
	/** 聊天群头像 */
	public static final String CHAT_GROUP_PORTRAIT = "chat_group_portrait";
	/** 聊天群号 */
	public static final String CHAT_GROUP_NUMBER = "chat_group_number";
	/** 聊天对象Id */
	/** 聊天对象账号 */
	/** 聊天对象昵称 */
	/** 聊天对象头像 */
	/** 是否为我发出的信息（1为我发出，2为对方发出） */
	/** 消息类型信息类型（1为文本，2为语音，3为图片，4为视频） */
	/** 消息内容 */
	/** 是否已经阅读 */
	/** 是否发送成功 */
	/** 发送时间 */

	// ************************************群表************************************//

	public static final String TB_GROUP = "tb_group";
	/** 群名称 */
	public static final String GROUP_NAME = "group_name";
	/** 官方群标识 0普通群，1官方群 */
	public static final String GROUP_OFFICIAL = "group_official";
	/** 群描述 */
	public static final String GROUP_DESCRIBE = "group_describe";
	/** 群成员上限 */
	public static final String GROUP_MAX = "group_max";
	/** 当前群成员人数 */
	public static final String MEMBER_COUNTS = "member_counts";
	/** 群公告 */
	public static final String GROUP_NOTICE = "group_notice";
	/** 是否群主 ：1是群主， 0非群主 */
	public static final String IS_OWNER = "is_owner";
	// ************************************设备列表************************************//
	/** 设备列表 */
	public static final String TB_MECHES = "tb_meches";
	/** 设备名称 */
	public static final String MECHE_NAME = "meches_name";
	/** 设备型号 */
	public static final String MECHE_MODEL = "meches_categary";
	/** 设备id */
	public static final String MECHE_ID = "meches_id";
	/** 设备描述 */
	public static final String MECHE_DRES = "meches_description";
	// ************************************设备类别菜单列表************************************//
	/** 设备类别表 */
	public static final String TB_CATEGORY_MECHE = "tb_category_meches";
	/** 设备类别名称 */
	public static final String CATEGORY_MECHE_NAME = "category_name";
	/** 设备类别id */
	public static final String CATEGORY_MECHE_ID = "category_id";
	/** 设备主类别id */
	public static final String CATEGORY_MAIN_ID = "did";
	/** 从属类别名称 */
	public static final String CATEGORY_TO_OF = "category_to_of";
	/** 设备类别描述 */
	public static final String CATEGORY_MECHE_DRES = "category_description";
	// ************************************推送拉取的消息列表************************************//
	/** 消息表 */
	public static final String TB_MESSAGE_SEND = "tb_message_send";
	/** 消息内容 */
	public static final String MESSAGE_SEND_CONTENT = "message_send_content";
	/** 消息id */
	public static final String MESSAGE_SEND__ID = "message_send_id";
	/** 消息时间 */
	public static final String MESSAGE_SEND_TIME = "message_send_time";
	/** 消息状态 */
	public static final String MESSAGE_SEND_STATUS = "message_send_status";
	/** 是否已读 */
	public static final String MESSAGE_SEND_IS_READ = "message_send_is_read";
	// ************************************订单列表************************************//
	/** 订单表 */
	public static final String TB_ORDERS = "tb_orders";
	/** 订单价格 */
	public static final String ORDERS_COUNT = "orders_count";
	/** 订单id */
	public static final String ORDERS_ID = "orders_id";
	/** 订单时间 */
	public static final String ORDERS_TIME = "orders_time";
	/** 订单状态 */
	public static final String ORDERS_STATUS = "orders_status";
	/** 订单描述 */
	public static final String ORDERS_DESCRIPTION = "orders_description";
	/** 订单地址 */
	public static final String ORDERS_ADDRES = "orders_addres";
	/** 订单地图坐标 */
	public static final String ORDERS_LOCATION = "orders_location";
	/** 订单接单人id */
	public static final String ORDERS_GETTER_ID = "orders_getter_id";
	/** 订单接单人名称 */
	public static final String ORDERS_GETTER_NAME = "orders_getter_name";
	/** 订单接单人电话号码 */
	public static final String ORDERS_GETTER_PHONE = "orders_getter_phone";
	/** 订单接单人所属公司 */
	public static final String ORDERS_GETTER_COMPANY = "orders_getter_company";
	// ************************************通用菜单表************************************//
	/** json通用字段 */
	public static final String TB_JSON = "tb_json";
	public static final String FIELD_JSON = "field_json";
	public static final String FIELD_TYPE = "field_type";
}
