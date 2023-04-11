-- 隧道分区表sd_tunnel_subarea添加字段direction

alter table sd_tunnel_subarea add column direction varchar(32) comment '分区方向' after tunnel_id;




-- 字典值：事件状态 sd_event_state, 添加字典数据：未处理
INSERT INTO sys_dict_data(dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark) VALUES (397, 0, '未处理', '3', 'sd_event_state', NULL, 'default', 'N', '0', 'admin', '2022-09-27 17:43:55', '', NULL, NULL);

-- 添加字典数据：隧道是否启用
INSERT INTO sys_dict_type(dict_id, dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark) VALUES (79, '隧道是否启用', 'sys_tunnel_use', '0', 'admin', '2022-09-14 17:27:33', '', NULL, NULL);
INSERT INTO sys_dict_data(dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark) VALUES (384, 0, '启用', '0', 'sys_tunnel_use', NULL, 'default', 'N', '0', 'admin', '2022-09-14 17:30:54', '', NULL, NULL);
INSERT INTO sys_dict_data(dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark) VALUES (385, 1, '停用', '1', 'sys_tunnel_use', NULL, 'default', 'N', '0', 'admin', '2022-09-14 17:31:28', '', NULL, NULL);


-- 添加字典数据：应急人员岗位
INSERT INTO sys_dict_type(dict_id, dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark) VALUES (85, '应急人员岗位', 'sd_emergency_post', '0', 'admin', '2022-10-20 14:13:31', '', NULL, NULL);
INSERT INTO sys_dict_data(dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark) VALUES (408, 0, '养护', '1', 'sd_emergency_post', NULL, 'default', 'N', '0', 'admin', '2022-10-20 14:13:47', '', NULL, NULL);
INSERT INTO sys_dict_data(dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark) VALUES (409, 0, '清障', '2', 'sd_emergency_post', NULL, 'default', 'N', '0', 'admin', '2022-10-20 14:13:57', '', NULL, NULL);


-- 隧道表添加字段sd_tunnels，start_pile、end_pile
alter table sd_tunnels add column start_pile varchar(20) comment '隧道开始桩号';
alter table sd_tunnels add column end_pile varchar(20) comment '隧道结束桩号';

-- 修改sd_equipment_state_icon_file表字段url的字段类型
alter table sd_equipment_state_icon_file modify column url longtext comment '图标地址';

-- 添加字典值：sd_warning_type_judge  预警类型判断符
INSERT INTO sys_dict_type(dict_id, dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark) VALUES (84, '预警类型判断符', 'sd_warning_type_judge', '0', 'admin', '2022-10-17 11:34:02', 'admin', '2022-10-17 11:59:44', '预警类型判断符列表');
INSERT INTO sys_dict_data(dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark) VALUES (405, 0, '大于', '0', 'sd_warning_type_judge', NULL, 'default', 'N', '0', 'admin', '2022-10-17 11:37:55', '', NULL, '大于');
INSERT INTO sys_dict_data(dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark) VALUES (406, 1, '小于', '1', 'sd_warning_type_judge', NULL, 'default', 'N', '0', 'admin', '2022-10-17 11:38:17', '', NULL, '小于');
INSERT INTO sys_dict_data(dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark) VALUES (407, 2, '等于', '2', 'sd_warning_type_judge', NULL, 'default', 'N', '0', 'admin', '2022-10-17 11:38:33', '', NULL, '等于');


-- 修改sd_strategy_rl表字段control_time的字段类型
alter table sd_strategy_rl modify column control_time varchar(20) comment '控制时间';


-- sys_dept 【部门表】字段类型、长度修改
alter table sys_dept modify column ancestors varchar(200) comment '祖级列表';
alter table sys_dept modify column dept_name varchar(200) comment '部门名称';
alter table sys_dept modify column order_num bigint(20) comment '显示顺序';
alter table sys_dept modify column create_by varchar(200) comment '创建人';
alter table sys_dept modify column update_by varchar(200) comment '修改人';

-- sys_user 【用户表】加入字段、长度修改
alter table sys_user add column jt_user_id varchar(50) comment '集团用户表的ID' after user_id;
alter table sys_user modify column user_name varchar(100) comment '用户账号';
alter table sys_user modify column nick_name varchar(50) comment '用户昵称';
alter table sys_user modify column avatar varchar(200) comment '头像地址';
-- 字典值：策略类型 sd_strategy_type 添加数据：分时控制

INSERT INTO sys_dict_data(dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark) VALUES (383, 4, '分时控制', '3', 'sd_strategy_type', NULL, 'default', 'N', '0', 'admin', '2022-09-12 09:58:31', '', NULL, NULL);

-- 应急物资权限
INSERT INTO  `sys_menu`( `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('删除', 1069, 3, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:material:remove', '#', 'admin', '2022-10-15 15:51:24', 'admin', '2022-10-15 15:53:09', '');
INSERT INTO  `sys_menu`( `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('修改', 1069, 2, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:material:edit', '#', 'admin', '2022-10-15 15:52:56', '', NULL, '');
-- 交通运行感知导出权限
INSERT INTO `sys_menu`(`menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('导出', 2272, 1, '', NULL, NULL, 1, 0, 'F', '0', '0', 'radar:data:export', '#', 'admin', '2022-10-15 14:54:42', '', NULL, '');

-- 情报板模板和情报板模板图片
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('情报板模板图片查询', 2233, '1',  '#', '', 1, 0, 'F', '0', '0', '',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('情报板模板图片新增', 2233, '2',  '#', '', 1, 0, 'F', '0', '0', '',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('情报板模板图片修改', 2233, '3',  '#', '', 1, 0, 'F', '0', '0', '',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('情报板模板图片删除', 2233, '4',  '#', '', 1, 0, 'F', '0', '0', '',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('情报板模板图片导出', 2233, '5',  '#', '', 1, 0, 'F', '0', '0', '',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('情报板模板查询', 2230, '1',  '#', '', 1, 0, 'F', '0', '0', '',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('情报板模板新增', 2230, '2',  '#', '', 1, 0, 'F', '0', '0', '',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('情报板模板修改', 2230, '3',  '#', '', 1, 0, 'F', '0', '0', '',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('情报板模板删除', 2230, '4',  '#', '', 1, 0, 'F', '0', '0', '',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('情报板模板导出', 2230, '5',  '#', '', 1, 0, 'F', '0', '0', '',       '#', 'admin', sysdate(), '', null, '');

-- 先删除字典：sd_control_type管控类别 的所有数据，再新增该类型下所有的字典数据

delete from sys_dict_data where dict_type = 'sd_control_type';
-- 字典表中sd_control_type枚举值替换
INSERT INTO `sys_dict_data`(`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (104, 0, '能见度', 'visibility', 'sd_control_type', NULL, 'default', 'N', '0', 'admin', '2022-02-12 14:18:35', '', NULL, NULL);
INSERT INTO `sys_dict_data`(`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (105, 1, '路面情况', 'road_condition', 'sd_control_type', NULL, 'default', 'N', '0', 'admin', '2022-02-12 14:19:33', '', NULL, NULL);
INSERT INTO `sys_dict_data`(`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (106, 2, '拥挤度', 'predicted_crowding', 'sd_control_type', NULL, 'default', 'N', '0', 'admin', '2022-02-12 14:21:46', 'admin', '2022-02-26 16:52:20', NULL);
INSERT INTO `sys_dict_data`(`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (198, 3, '突发事件', 'emergency_incident', 'sd_control_type', NULL, 'default', 'N', '0', 'admin', '2022-03-14 11:11:34', '', NULL, NULL);
INSERT INTO `sys_dict_data`(`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (400, 8, '光强控制', '8', 'sd_control_type', NULL, 'default', 'N', '0', 'admin', '2022-10-09 16:59:34', '', NULL, NULL);
INSERT INTO `sys_dict_data`(`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (410, 4, '手动控制', '0', 'sd_control_type', NULL, 'default', 'N', '0', 'admin', '2021-12-17 14:52:12', '', NULL, NULL);
INSERT INTO `sys_dict_data`(`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (411, 5, '定时控制', '1', 'sd_control_type', NULL, 'default', 'N', '0', 'admin', '2021-12-17 14:52:29', '', NULL, NULL);
INSERT INTO `sys_dict_data`(`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (412, 6, '自动触发', '2', 'sd_control_type', NULL, 'default', 'N', '0', 'admin', '2022-09-02 11:36:39', 'admin', '2022-09-02 11:37:07', NULL);
INSERT INTO `sys_dict_data`(`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (413, 7, '分时控制', '3', 'sd_control_type', NULL, 'default', 'N', '0', 'admin', '2022-09-12 09:58:31', '', NULL, NULL);

-- 隧道管理新增权限
INSERT INTO `sys_menu`( `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('配置', 1074, 1, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:tunnels:edit', '#', 'admin', '2022-10-15 15:23:56', '', NULL, '');
INSERT INTO `sys_menu`( `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('修改', 1074, 2, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:tunnels:edit', '#', 'admin', '2022-10-15 15:29:20', '', NULL, '');
INSERT INTO `sys_menu`( `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('删除', 1074, 3, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:tunnels:remove', '#', 'admin', '2022-10-15 15:30:31', '', NULL, '');
INSERT INTO `sys_menu`( `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('新增', 1074, 4, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:tunnels:add', '#', 'admin', '2022-10-18 11:52:48', '', NULL, '');

-- 应急资源权限
INSERT INTO `sys_menu`( `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('删除', 1069, 3, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:material:remove', '#', 'admin', '2022-10-15 15:51:24', 'admin', '2022-10-15 15:53:09', '');
INSERT INTO `sys_menu`( `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('修改', 1069, 2, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:material:edit', '#', 'admin', '2022-10-15 15:52:56', '', NULL, '');

-- 数据库字典权限（设备图标、设备管理、设备档案管理、环境配置、预警事件）
INSERT INTO `sys_menu`( `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2297, '修改', 1310, 1, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:component:edit', '#', 'admin', '2022-10-15 15:06:05', '', NULL, '');
INSERT INTO `sys_menu`( `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2298, '删除', 1310, 2, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:component:remove', '#', 'admin', '2022-10-15 15:06:35', '', NULL, '');
INSERT INTO `sys_menu`( `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2299, '修改', 2173, 1, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:configuration:edit', '#', 'admin', '2022-10-15 15:12:24', '', NULL, '');
INSERT INTO `sys_menu`( `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2300, '删除', 2173, 2, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:configuration:remove', '#', 'admin', '2022-10-15 15:13:10', '', NULL, '');
INSERT INTO `sys_menu`( `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2301, '删除', 1077, 2, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:type:remove', '#', 'admin', '2022-10-15 15:19:03', '', NULL, '');
INSERT INTO `sys_menu`( `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2302, '修改', 1077, 1, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:type:edit', '#', 'admin', '2022-10-15 15:20:23', '', NULL, '');
INSERT INTO `sys_menu`( `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('控制修改', 1100, 1, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:devices:edit', '#', 'admin', '2022-10-15 15:34:05', '', NULL, '');
INSERT INTO `sys_menu`( `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('删除', 1100, 2, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:devices:remove', '#', 'admin', '2022-10-15 15:36:06', '', NULL, '');
INSERT INTO `sys_menu`( `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('新增', 1310, 3, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:component:add', '#', 'admin', '2022-10-18 11:34:20', '', NULL, '');
INSERT INTO `sys_menu`( `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('导出', 2173, 3, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:configuration:export', '#', 'admin', '2022-10-18 11:49:34', '', NULL, '');
INSERT INTO `sys_menu`( `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('新增', 1100, 3, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:devices:add', '#', 'admin', '2022-10-18 11:56:57', '', NULL, '');
INSERT INTO `sys_menu`( `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('修改', 1100, 4, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:devices:edit', '#', 'admin', '2022-10-18 11:57:13', '', NULL, '');
INSERT INTO `sys_menu`( `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('导出', 1100, 5, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:devices:export', '#', 'admin', '2022-10-18 11:58:00', '', NULL, '');
INSERT INTO `sys_menu`( `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('导入', 1100, 6, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:devices:import', '#', 'admin', '2022-10-18 11:58:16', '', NULL, '');
INSERT INTO `sys_menu`( `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('忽略', 2252, 1, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:event:edit', '#', 'admin', '2022-10-19 08:54:22', '', NULL, '');
INSERT INTO `sys_menu`( `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('已解决', 2252, 2, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:event:edit', '#', 'admin', '2022-10-19 08:54:43', '', NULL, '');
INSERT INTO `sys_menu`( `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('查看详情', 2252, 3, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:event:remove', '#', 'admin', '2022-10-19 08:55:00', '', NULL, '');
INSERT INTO `sys_menu`( `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('应急调度', 2252, 4, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:event:remove', '#', 'admin', '2022-10-19 08:55:21', '', NULL, '');


-- 删除sd_devices_copy表

-- 自动触发定时任务
INSERT INTO `sys_job`(job_name,job_group,invoke_target,cron_expression,misfire_policy,concurrent,status,remark,create_by,create_time)VALUES ('自动触发任务', 'DEFAULT', 'strategyTask.triggerJob()', '0 0/5 * * * ?', '1', '0', '0', '','', '2022-10-27 10:39:29');

-- 隧道表桩号字段
alter table sd_tunnels add column start_pile_num varchar(20) comment '隧道开始桩号（整型）';
alter table sd_tunnels add column end_pile_num varchar(20) comment '隧道结束桩号（整型）';

-- 隧道分区表桩号字段
alter table sd_tunnel_subarea add column start_pile varchar(20) comment '开始桩号';
alter table sd_tunnel_subarea add column end_pile varchar(20) comment '结束桩号';

-- 事件类型sd_event_type
alter table sd_event_type add column f_id varchar(20) comment '父类ID';
alter table sd_event_type add column simplify_name varchar(20) comment '类型简称';

-- 设备控制记录表 sd_operation_log
alter table sd_operation_log add column oper_ip varchar(100) comment 'IP地址';

-- 数据字典 设备控制结果
insert into `sys_dict_type`( `dict_name`, `dict_type`, `status`, `create_by`, `create_time` ) values( '操作状态', 'sd_device_opt_state', '0', 'admin', sysdate() );
insert into `sys_dict_data`( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `status`, `create_by`, `create_time` ) values( 0, '失败', '0', 'sd_device_opt_state', 'default', '0', 'admin', sysdate() );
insert into `sys_dict_data`( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `status`, `create_by`, `create_time` ) values( 1, '成功', '1', 'sd_device_opt_state', 'default', '0', 'admin', sysdate() );
--数据字典  设备控制方式-预案执行添加
INSERT INTO `sys_dict_data` ( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `status`, `create_by`, `create_time` ) VALUES ( 8, '预案执行', '4', 'sd_control_type', 'default', '0', 'admin',  sysdate());
--设备类型 sd_equipment_type
alter table sd_equipment_type add column is_analog varchar(10) comment '是否模拟量设备1：是 0：否(用于触发策略筛选)';

-- -- 数据字典 设备所属系统
-- INSERT INTO `sys_dict_data` VALUES (0, '监控系统', '0', 'eq_system', NULL, 'default', 'N', '0', 'admin', '2022-11-04 14:14:36', '', NULL, NULL);
-- INSERT INTO `sys_dict_data` VALUES (1, '通风系统', '1', 'eq_system', NULL, 'default', 'N', '0', 'admin', '2022-11-04 14:14:55', '', NULL, NULL);
-- INSERT INTO `sys_dict_data` VALUES (2, '照明系统', '2', 'eq_system', NULL, 'default', 'N', '0', 'admin', '2022-11-04 14:15:13', '', NULL, NULL);
-- INSERT INTO `sys_dict_data` VALUES (3, '供配电系统', '3', 'eq_system', NULL, 'default', 'N', '0', 'admin', '2022-11-04 14:15:27', 'admin', '2022-11-04 14:15:58', NULL);
-- INSERT INTO `sys_dict_data` VALUES (463, 4, '消防系统', '4', 'eq_system', NULL, 'default', 'N', '0', 'admin', '2022-11-04 14:15:45', '', NULL, NULL);

--数据字典 故障类型
insert into `sys_dict_data`( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `status`, `create_by`, `create_time` ) values( 1, '自然损坏', '0', 'fault_type', 'default', '0', 'admin', sysdate() );
insert into `sys_dict_data`( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `status`, `create_by`, `create_time` ) values( 2, '腐蚀泡水', '1', 'fault_type', 'default', '0', 'admin', sysdate() );
insert into `sys_dict_data`( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `status`, `create_by`, `create_time` ) values( 3, '变形或断裂', '2', 'fault_type', 'default', '0', 'admin', sysdate() );
insert into `sys_dict_data`( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `status`, `create_by`, `create_time` ) values( 4, '间歇性故障', '3', 'fault_type', 'default', '0', 'admin', sysdate() );
insert into `sys_dict_data`( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `status`, `create_by`, `create_time` ) values( 5, '机械故障', '4', 'fault_type', 'default', '0', 'admin', sysdate() );
insert into `sys_dict_data`( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `status`, `create_by`, `create_time` ) values( 6, '人为损坏', '5', 'fault_type', 'default', '0', 'admin', sysdate() );
insert into `sys_dict_data`( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `status`, `create_by`, `create_time` ) values( 7, '其他', '6', 'fault_type', 'default', '0', 'admin', sysdate() );
--数据字典  故障等级
insert into `sys_dict_data`( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `status`, `create_by`, `create_time` ) values( 1, '一般故障', '0', 'fault_level', 'default', '0', 'admin', sysdate() );
insert into `sys_dict_data`( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `status`, `create_by`, `create_time` ) values( 2, '严重故障', '1', 'fault_level', 'default', '0', 'admin', sysdate() );
insert into `sys_dict_data`( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `status`, `create_by`, `create_time` ) values( 3, '重大故障', '2', 'fault_level', 'default', '0', 'admin', sysdate() );
--数据字典  故障消除状态
insert into `sys_dict_data`( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `status`, `create_by`, `create_time` ) values( 1, '已消除', '0', 'fault_remove_statue', 'default', '0', 'admin', sysdate() );
insert into `sys_dict_data`( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `status`, `create_by`, `create_time` ) values( 2, '未消除', '1', 'fault_remove_statue', 'default', '0', 'admin', sysdate() );
--数据字典  故障状态
insert into `sys_dict_data`( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `status`, `create_by`, `create_time` ) values( 1, '已发布', '0', 'fault_status', 'default', '0', 'admin', sysdate() );
insert into `sys_dict_data`( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `status`, `create_by`, `create_time` ) values( 2, '未发布', '1', 'fault_status', 'default', '0', 'admin', sysdate() );
--数据字典  故障处理情况
insert into `sys_dict_data`( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `status`, `create_by`, `create_time` ) values( 1, '无故障', '0', 'fault_clstatus', 'default', '0', 'admin', sysdate() );
insert into `sys_dict_data`( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `status`, `create_by`, `create_time` ) values( 2, '已消除', '1', 'fault_clstatus', 'default', '0', 'admin', sysdate() );
insert into `sys_dict_data`( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `status`, `create_by`, `create_time` ) values( 3, '未消除', '2', 'fault_clstatus', 'default', '0', 'admin', sysdate() );

--机电巡查数据表
--故障清单表
CREATE TABLE `sd_fault_list`  (
  `id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '故障编号',
  `tunnel_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '隧道id',
  `fault_location` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '故障位置（设备的“方向”、“桩号”，拼接展示）',
  `fault_type` int(2) NULL DEFAULT NULL COMMENT '故障类型(0:自然损坏；1：腐蚀泡水；2：变形或断裂；3：间歇性故障；4：机械故障；5：人为损坏；6：其他)',
  `fault_source` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '故障发现源',
  `fault_fxtime` datetime(0) NULL DEFAULT NULL COMMENT '故障发现时间',
  `fault_cxtime` varchar(0) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '故障持续时间（根据当前时间与故障发现时间计算时间差，单位：天、小时；计算时间差，不保存只展示；记录变为“已消除”状态时，保存持续时间）',
  `fault_tbr` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '故障填报人',
  `fault_tbtime` datetime(0) NULL DEFAULT NULL COMMENT '故障填报时间',
  `eq_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备id',
  `eq_status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备状态（1-在线，2-离线，3-故障）',
  `fault_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '故障代码',
  `fault_level` int(2) NULL DEFAULT NULL COMMENT '故障等级',
  `fallt_remove_statue` int(2) NULL DEFAULT NULL COMMENT '故障消除状态（0：已消除；1：未消除）',
  `fault_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '故障描述',
  `fault_status` int(2) NULL DEFAULT NULL COMMENT '状态（0：已发布；1：未发布）',
  `img_file_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片id',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '故障清单表' ROW_FORMAT = Dynamic;
--巡检任务表
CREATE TABLE `sd_task_list`  (
  `id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '任务编号',
  `zzjg_id` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所属单位id(新增时保存当前登录人的单位id)',
  `end_plantime` datetime(0) NULL DEFAULT NULL COMMENT '计划完成时间',
  `dispatcher` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '派单人员',
  `dispatch_time` datetime(0) NULL DEFAULT NULL COMMENT '派单时间',
  `bz_id` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '指派巡查班组id',
  `task_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务描述',
  `publish_status` int(2) NULL DEFAULT NULL COMMENT '发布状态(0:未发布状态;1:已废止状态;2:发布状态)',
  `task_status` int(2) NULL DEFAULT NULL COMMENT '任务状态（0::待巡检、1:巡检中、2:已完结、3:待回传、4:超时）',
  `walker_id` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡查人员id',
  `task_endtime` datetime(0) NULL DEFAULT NULL COMMENT '任务完成时间',
  `task_cxtime` datetime(0) NULL DEFAULT NULL COMMENT '任务持续时间',
  `site_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '现场情况描述',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '巡查任务表' ROW_FORMAT = Dynamic;
--巡检任务操作记录表
CREATE TABLE `sd_task_opt`  (
  `id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `task_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡检任务id',
  `opt_type` int(2) NULL DEFAULT NULL COMMENT '操作类型（0:派单（发布）;1:接收;2:提交）',
  `opt_person_id` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `opt_time` datetime(0) NULL DEFAULT NULL COMMENT '操作时间',
  `opt_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作说明',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '巡检任务操作记录表' ROW_FORMAT = Dynamic;
--巡检点清单表
CREATE TABLE `sd_patrol_list`  (
  `id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `task_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡检任务id',
  `eq_fault_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备id或故障清单表id',
  `patrol_type` int(2) NULL DEFAULT NULL COMMENT '巡检点类型（0：巡检点；1：故障点）',
  `eq_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备或巡检点名称',
  `position` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '安装位置',
  `impression` int(2) NULL DEFAULT NULL COMMENT '外观情况(0:外观完整;2:外观破损)',
  `network` int(2) NULL DEFAULT NULL COMMENT '网络通讯情况(0:通讯正常;1:通讯异常)',
  `power` int(2) NULL DEFAULT NULL COMMENT '供配电情况(0:配电正常;1:配电异常)',
  `eq_status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备状态',
  `run_status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '运行状态',
  `eq_fault_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备故障码（碰一碰检测）',
  `eq_fault_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备故障描述',
  `fault_clstatus` int(2) NULL DEFAULT NULL COMMENT '故障处理情况（0：无故障:；1：已消除:；2：未消除）',
  `xc_time` datetime(0) NULL DEFAULT NULL COMMENT '巡查时间',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `xc_sort` int(2) NULL DEFAULT NULL COMMENT '巡查顺序',
  `xc_status` int(2) NULL DEFAULT NULL COMMENT '巡查状态（0：未巡检；1：已巡检）',
  `img_file_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片id',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '巡查点清单表' ROW_FORMAT = Dynamic;

-- 设备备件库 所属隧道ID
alter table sd_spare_parts_warehouse add column tunnel_id varchar(100) comment '所属隧道ID' after id;

-- 新增推送数据历史记录表
DROP TABLE IF EXISTS `sd_push_history`;
CREATE TABLE `sd_push_history`  (
   `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
   `data_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '推送接口 device、tunnel',
   `push_data` varchar(3000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '推送数据',
   `push_status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '状态 0：未推送 1：已推送',
   `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
   `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
   PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '推送数据历史记录表' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;

-- 设备类型sd_equipment_type
alter table sd_equipment_type add column eq_category varchar(10) comment '设备大类';
alter table sd_equipment_type add column eq_system varchar(10) comment '所属系统';

-- 设备控制记录表事件ID
alter table sd_operation_log add column event_id varchar(100) comment '事件ID';

-- 数据字典设备所属系统
INSERT INTO sys_dict_type(dict_id, dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark) VALUES (101, '所属系统', 'eq_system', '0', 'admin', '2022-11-04 14:12:57', '', NULL, NULL);
INSERT INTO `sys_dict_data` ( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`,`list_class`,`is_default`, `status`, `create_by`, `create_time`,`update_by`,`update_time`,`remark` ) VALUES ( 0, '监控系统', '0', 'eq_system', NULL, 'default', 'N', '0', 'admin', '2022-11-04 14:14:36', '', NULL, NULL);
INSERT INTO `sys_dict_data` ( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`,`list_class`,`is_default`, `status`, `create_by`, `create_time`,`update_by`,`update_time`,`remark` ) VALUES ( 1, '通风系统', '1', 'eq_system', NULL, 'default', 'N', '0', 'admin', '2022-11-04 14:14:55', '', NULL, NULL);
INSERT INTO `sys_dict_data` ( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`,`list_class`,`is_default`, `status`, `create_by`, `create_time`,`update_by`,`update_time`,`remark` ) VALUES ( 2, '照明系统', '2', 'eq_system', NULL, 'default', 'N', '0', 'admin', '2022-11-04 14:15:13', '', NULL, NULL);
INSERT INTO `sys_dict_data` ( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`,`list_class`,`is_default`, `status`, `create_by`, `create_time`,`update_by`,`update_time`,`remark` ) VALUES ( 3, '供配电系统', '3', 'eq_system', NULL, 'default', 'N', '0', 'admin', '2022-11-04 14:15:27', 'admin', '2022-11-04 14:15:58', NULL);
INSERT INTO `sys_dict_data` ( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`,`list_class`,`is_default`, `status`, `create_by`, `create_time`,`update_by`,`update_time`,`remark` ) VALUES ( 4, '消防系统', '4', 'eq_system', NULL, 'default', 'N', '0', 'admin', '2022-11-04 14:15:45', '', NULL, NULL);

-- 数据字典设备所属大类
INSERT INTO sys_dict_type(dict_id, dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark) VALUES (102, '设备大类', 'eq_category', '0', 'admin', '2022-11-07 10:04:01', '', NULL, NULL);
INSERT INTO `sys_dict_data` ( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`,`list_class`,`is_default`, `status`, `create_by`, `create_time`,`update_by`,`update_time`,`remark` ) VALUES ( 0, '车道指示器', '0', 'eq_category', NULL, 'default', 'N', '0', 'admin', '2022-11-07 10:04:44', 'admin', '2022-11-07 10:09:11', NULL);
INSERT INTO `sys_dict_data` ( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`,`list_class`,`is_default`, `status`, `create_by`, `create_time`,`update_by`,`update_time`,`remark` ) VALUES ( 1, '交通信号灯', '1', 'eq_category', NULL, 'default', 'N', '0', 'admin', '2022-11-07 10:04:51', 'admin', '2022-11-07 10:09:22', NULL);
INSERT INTO `sys_dict_data` ( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`,`list_class`,`is_default`, `status`, `create_by`, `create_time`,`update_by`,`update_time`,`remark` ) VALUES ( 2, '亮度检测器', '2', 'eq_category', NULL, 'default', 'N', '0', 'admin', '2022-11-07 10:05:00', 'admin', '2022-11-07 10:09:30', NULL);
INSERT INTO `sys_dict_data` ( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`,`list_class`,`is_default`, `status`, `create_by`, `create_time`,`update_by`,`update_time`,`remark` ) VALUES ( 3, '摄像机', '3', 'eq_category', NULL, 'default', 'N', '0', 'admin', '2022-11-07 10:05:10', 'admin', '2022-11-07 10:09:38', NULL);
INSERT INTO `sys_dict_data` ( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`,`list_class`,`is_default`, `status`, `create_by`, `create_time`,`update_by`,`update_time`,`remark` ) VALUES ( 4, '紧急电话', '4', 'eq_category', NULL, 'default', 'N', '0', 'admin', '2022-11-07 10:05:21', 'admin', '2022-11-07 10:09:45', NULL);
INSERT INTO `sys_dict_data` ( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`,`list_class`,`is_default`, `status`, `create_by`, `create_time`,`update_by`,`update_time`,`remark` ) VALUES ( 5, '火灾报警', '5', 'eq_category', NULL, 'default', 'N', '0', 'admin', '2022-11-07 10:05:31', 'admin', '2022-11-07 10:09:59', NULL);
INSERT INTO `sys_dict_data` ( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`,`list_class`,`is_default`, `status`, `create_by`, `create_time`,`update_by`,`update_time`,`remark` ) VALUES ( 6, '巡检机器人', '6', 'eq_category', NULL, 'default', 'N', '0', 'admin', '2022-11-07 10:05:40', 'admin', '2022-11-07 10:10:08', NULL);
INSERT INTO `sys_dict_data` ( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`,`list_class`,`is_default`, `status`, `create_by`, `create_time`,`update_by`,`update_time`,`remark` ) VALUES ( 7, '智能消防炮', '7', 'eq_category', NULL, 'default', 'N', '0', 'admin', '2022-11-07 10:05:49', 'admin', '2022-11-07 10:10:17', NULL);
INSERT INTO `sys_dict_data` ( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`,`list_class`,`is_default`, `status`, `create_by`, `create_time`,`update_by`,`update_time`,`remark` ) VALUES ( 8, '可变信息标志', '8', 'eq_category', NULL, 'default', 'N', '0', 'admin', '2022-11-07 10:05:57', 'admin', '2022-11-07 10:10:27', NULL);
INSERT INTO `sys_dict_data` ( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`,`list_class`,`is_default`, `status`, `create_by`, `create_time`,`update_by`,`update_time`,`remark` ) VALUES ( 9, '雷达', '9', 'eq_category', NULL, 'default', 'N', '0', 'admin', '2022-11-07 10:06:06', 'admin', '2022-11-07 10:10:35', NULL);
INSERT INTO `sys_dict_data` ( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`,`list_class`,`is_default`, `status`, `create_by`, `create_time`,`update_by`,`update_time`,`remark` ) VALUES ( 10, 'PLC主机', '10', 'eq_category', NULL, 'default', 'N', '0', 'admin', '2022-11-07 10:06:16', 'admin', '2022-11-07 10:10:45', NULL);
INSERT INTO `sys_dict_data` ( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`,`list_class`,`is_default`, `status`, `create_by`, `create_time`,`update_by`,`update_time`,`remark` ) VALUES ( 11, '微波车辆检测器', '11', 'eq_category', NULL, 'default', 'N', '0', 'admin', '2022-11-07 10:06:23', 'admin', '2022-11-07 10:10:51', NULL);
INSERT INTO `sys_dict_data` ( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`,`list_class`,`is_default`, `status`, `create_by`, `create_time`,`update_by`,`update_time`,`remark` ) VALUES ( 12, 'CO/VI检测器', '12', 'eq_category', NULL, 'default', 'N', '0', 'admin', '2022-11-07 10:06:30', 'admin', '2022-11-07 10:10:58', NULL);
INSERT INTO `sys_dict_data` ( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`,`list_class`,`is_default`, `status`, `create_by`, `create_time`,`update_by`,`update_time`,`remark` ) VALUES ( 13, '风速风向检测器', '13', 'eq_category', NULL, 'default', 'N', '0', 'admin', '2022-11-07 10:06:38', 'admin', '2022-11-07 10:11:06', NULL);
INSERT INTO `sys_dict_data` ( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`,`list_class`,`is_default`, `status`, `create_by`, `create_time`,`update_by`,`update_time`,`remark` ) VALUES ( 14, '智能诱导灯', '14', 'eq_category', NULL, 'default', 'N', '0', 'admin', '2022-11-07 10:06:46', 'admin', '2022-11-07 10:11:14', NULL);
INSERT INTO `sys_dict_data` ( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`,`list_class`,`is_default`, `status`, `create_by`, `create_time`,`update_by`,`update_time`,`remark` ) VALUES ( 15, '智能疏散标志', '15', 'eq_category', NULL, 'default', 'N', '0', 'admin', '2022-11-07 10:06:57', 'admin', '2022-11-07 10:11:22', NULL);
INSERT INTO `sys_dict_data` ( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`,`list_class`,`is_default`, `status`, `create_by`, `create_time`,`update_by`,`update_time`,`remark` ) VALUES ( 16, '风机', '16', 'eq_category', NULL, 'default', 'N', '0', 'admin', '2022-11-07 10:07:06', 'admin', '2022-11-07 10:11:30', NULL);
INSERT INTO `sys_dict_data` ( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`,`list_class`,`is_default`, `status`, `create_by`, `create_time`,`update_by`,`update_time`,`remark` ) VALUES ( 17, '照明', '17', 'eq_category', NULL, 'default', 'N', '0', 'admin', '2022-11-07 10:07:15', 'admin', '2022-11-07 10:11:39', NULL);
INSERT INTO `sys_dict_data` ( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`,`list_class`,`is_default`, `status`, `create_by`, `create_time`,`update_by`,`update_time`,`remark` ) VALUES ( 18, '卷帘门', '18', 'eq_category', NULL, 'default', 'N', '0', 'admin', '2022-11-07 10:07:24', 'admin', '2022-11-07 10:11:47', NULL);
INSERT INTO `sys_dict_data` ( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`,`list_class`,`is_default`, `status`, `create_by`, `create_time`,`update_by`,`update_time`,`remark` ) VALUES ( 19, '消防栓', '19', 'eq_category', NULL, 'default', 'N', '0', 'admin', '2022-11-07 10:07:32', 'admin', '2022-11-07 10:11:56', NULL);
INSERT INTO `sys_dict_data` ( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`,`list_class`,`is_default`, `status`, `create_by`, `create_time`,`update_by`,`update_time`,`remark` ) VALUES ( 20, '远传压力表', '20', 'eq_category', NULL, 'default', 'N', '0', 'admin', '2022-11-07 10:07:47', 'admin', '2022-11-07 10:12:18', NULL);
INSERT INTO `sys_dict_data` ( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`,`list_class`,`is_default`, `status`, `create_by`, `create_time`,`update_by`,`update_time`,`remark` ) VALUES ( 21, '消防水泵', '21', 'eq_category', NULL, 'default', 'N', '0', 'admin', '2022-11-07 10:07:56', 'admin', '2022-11-07 10:12:28', NULL);
INSERT INTO `sys_dict_data` ( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`,`list_class`,`is_default`, `status`, `create_by`, `create_time`,`update_by`,`update_time`,`remark` ) VALUES ( 22, '液位传感器', '22', 'eq_category', NULL, 'default', 'N', '0', 'admin', '2022-11-07 10:08:03', 'admin', '2022-11-07 10:12:40', NULL);
INSERT INTO `sys_dict_data` ( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`,`list_class`,`is_default`, `status`, `create_by`, `create_time`,`update_by`,`update_time`,`remark` ) VALUES ( 23, '消防水池', '23', 'eq_category', NULL, 'default', 'N', '0', 'admin', '2022-11-07 10:08:13', 'admin', '2022-11-07 10:12:48', NULL);
INSERT INTO `sys_dict_data` ( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`,`list_class`,`is_default`, `status`, `create_by`, `create_time`,`update_by`,`update_time`,`remark` ) VALUES ( 24, '光伏棚洞', '24', 'eq_category', NULL, 'default', 'N', '0', 'admin', '2022-11-07 10:08:23', 'admin', '2022-11-07 10:12:56', NULL);
INSERT INTO `sys_dict_data` ( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`,`list_class`,`is_default`, `status`, `create_by`, `create_time`,`update_by`,`update_time`,`remark` ) VALUES ( 25, '变电所', '25', 'eq_category', NULL, 'default', 'N', '0', 'admin', '2022-11-07 10:08:33', 'admin', '2022-11-07 10:13:05', NULL);

-- 情报板设备类型字典值
INSERT INTO `sys_dict_type`(`dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('情报板设备类型', 'iot_devices_type', '0', 'admin', '2022-11-09 10:34:30', '', NULL, '对应iot_device表中local_info');
INSERT INTO `sys_dict_data`(`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1, '洞口设备', '0', 'iot_devices_type', NULL, 'default', 'N', '0', 'admin', '2022-11-09 10:35:34', '', NULL, '情报板设备位置信息洞口设备');
INSERT INTO `sys_dict_data`(`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2, '洞内设备', '1', 'iot_devices_type', NULL, 'default', 'N', '0', 'admin', '2022-11-09 10:36:21', '', NULL, '情报板设备位置信息洞内设备');
-- 情报板模板类别字典值增加
INSERT INTO `sys_dict_type`(`dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('情报板模板分类', 'iot_template_category', '0', 'admin', '2022-11-09 14:28:44', '', NULL, '情报板管理页面模板分类');
INSERT INTO  `sys_dict_data`( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1, '日常通用', '0', 'iot_template_category', NULL, 'default', 'N', '0', 'admin', '2022-11-09 14:31:19', '', NULL, '模板类别');
INSERT INTO  `sys_dict_data`( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2, '施工管控', '1', 'iot_template_category', NULL, 'default', 'N', '0', 'admin', '2022-11-09 14:32:01', '', NULL, '模板类别');
INSERT INTO  `sys_dict_data`( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (3, '恶劣天气', '2', 'iot_template_category', NULL, 'default', 'N', '0', 'admin', '2022-11-09 14:32:32', '', NULL, '模板类别');
INSERT INTO  `sys_dict_data`( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (4, '交通拥堵', '3', 'iot_template_category', NULL, 'default', 'N', '0', 'admin', '2022-11-09 14:33:17', '', NULL, '模板类别');
INSERT INTO  `sys_dict_data`( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (5, '突发事件', '4', 'iot_template_category', NULL, 'default', 'N', '0', 'admin', '2022-11-09 14:34:07', '', NULL, '模板类别');
INSERT INTO  `sys_dict_data`( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (6, '警情播报', '5', 'iot_template_category', NULL, 'default', 'N', '0', 'admin', '2022-11-09 14:35:05', '', NULL, '模板类别');
-- 情报板模板数据库表新增字段
alter table iot_board_template add column category varchar(100) comment '模板类别';

-- 数据字典防空类型
INSERT INTO `sys_dict_type`(`dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ( '防控类型', 'prev_control_type', '0', 'admin', '2022-11-09 10:37:37', '', NULL, NULL);INSERT INTO `sys_dict_data` ( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`,`list_class`,`is_default`, `status`, `create_by`, `create_time`,`update_by`,`update_time`,`remark` ) VALUES ( 0, '监控系统', '0', 'eq_system', NULL, 'default', 'N', '0', 'admin', '2022-11-04 14:14:36', '', NULL, NULL);
INSERT INTO `sys_dict_data`(`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ( 0, '交通事件', '0', 'prev_control_type', NULL, 'default', 'N', '0', 'admin', '2022-11-09 10:38:17', '', NULL, NULL);
INSERT INTO `sys_dict_data`(`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ( 1, '主动安全', '1', 'prev_control_type', NULL, 'default', 'N', '0', 'admin', '2022-11-09 10:38:32', '', NULL, NULL);
INSERT INTO `sys_dict_data`(`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ( 2, '设备故障', '2', 'prev_control_type', NULL, 'default', 'N', '0', 'admin', '2022-11-09 10:38:48', '', NULL, NULL);

-- 新增菜单，机电巡查
INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES (2339, '机电巡查', 0, 6, 'empatrol', NULL, NULL, 1, 0, 'M', '0', '0', '', '#', 'admin', '2022-11-02 16:35:31', 'admin', '2022-11-03 09:26:32', '');
INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES (2341, '故障管理', 2339, 1, 'emFault', 'electromechanicalPatrol/faultManage/index', NULL, 1, 0, 'C', '0', '0', '', '#', 'admin', '2022-11-03 09:15:14', 'admin', '2022-11-04 15:07:20', '');
INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES (2345, '巡查任务', 2339, 2, 'emTask', 'electromechanicalPatrol/taskManage/index', NULL, 1, 0, 'C', '0', '0', '', '#', 'admin', '2022-11-04 14:40:01', 'admin', '2022-11-04 15:25:24', '');

-- 新增菜单，安全预警
INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES (2338, '安全预警', 2250, 1, 'safeWarn', 'event/event/safeWarn', NULL, 1, 0, 'C', '0', '0', '', '#', 'admin', '2022-11-02 10:01:48', 'admin', '2022-11-02 16:55:52', '');

-- 2022-11-14 start

-- 修改sd_traffic_image表字段img_url的字段类型
alter table sd_traffic_image modify column img_url longtext comment '图片url';

-- 修改sd_task_list表字段task_cxtime的字段类型
alter table sd_task_list column task_cxtime varchar(25) comment '任务持续时间';

-- 修改sd_fault_list表字段fault_cxtime的字段类型
alter table sd_fault_list column fault_cxtime varchar(25) comment '故障持续时间（根据当前时间与故障发现时间计算时间差，单位：天、小时；计算时间差，不保存只展示；记录变为“已消除”状态时，保存持续时间）';

-- 数据字典新增设备模拟控制开关标识
INSERT INTO `sys_dict_type`(`dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('设备控制模拟值', 'sys_analog_control_isopen', '0', 'admin', '2022-11-14 11:01:31', '', NULL, NULL);
INSERT INTO `sys_dict_data`(`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (0, '是否开启设备模拟控制', '1', 'sys_analog_control_isopen', NULL, 'default', 'N', '0', 'admin', '2022-11-14 11:44:13', '', NULL, '1：开启模拟控制；0：关闭模拟控制');

-- 事件类型
alter table sd_event_type DROP COLUMN f_id;
alter table sd_event_type add column icon_url longtext comment '图标base64';
alter table sd_event_type add column prev_control_type varchar(20) comment '防控类型';



-- 隧道表sd_tunnels，诱导灯重新配置
-- 设备表sd_devices，诱导灯数据修改,建议只导入诱导灯数据
-- 字典表sys_dict_type、sys_dict_data：所属系统，字典值修改，建议整张表重新导入
-- 设备类型表sd_equipment_type，设备类型、代号修改，诱导灯图片大小，建议整张表重新导入
-- 疏散标志图片修改，图片重新上传
-- 事件类型表sd_event_type，建议整张表重新导入
-- 设备类型数据项修改
UPDATE sd_device_type_item set remark = "疏散标志控制模式(1：关灯；2：同步单闪；3：逆向流水;4:左闪;5:双闪;6:右闪)" WHERE id = 19

-- 修改sd_traffic_image表字段img_url的字段类型
alter table sd_traffic_image modify column img_url longtext comment '图片url';

-- 修改sd_task_list表字段task_cxtime的字段类型
alter table sd_task_list modify column task_cxtime varchar(25) comment '任务持续时间';

-- 修改sd_fault_list表字段fault_cxtime的字段类型
alter table sd_fault_list modify column fault_cxtime varchar(25) comment '故障持续时间（根据当前时间与故障发现时间计算时间差，单位：天、小时；计算时间差，不保存只展示；记录变为“已消除”状态时，保存持续时间）';

-- 数据字典 任务发布状态
insert into `sys_dict_type`( `dict_name`, `dict_type`, `status`, `create_by`, `create_time` ) values( '任务发布状态', 'publish_status', '0', 'admin', sysdate() );
insert into `sys_dict_data`( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `status`, `create_by`, `create_time` ) values( 1, '未发布状态', '0', 'publish_status', 'default', '0', 'admin', sysdate() );
insert into `sys_dict_data`( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `status`, `create_by`, `create_time` ) values( 2, '已废止状态', '1', 'publish_status', 'default', '0', 'admin', sysdate() );
insert into `sys_dict_data`( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `status`, `create_by`, `create_time` ) values( 3, '发布状态', '2', 'publish_status', 'default', '0', 'admin', sysdate() );

-- 数据字典 任务状态
insert into `sys_dict_type`( `dict_name`, `dict_type`, `status`, `create_by`, `create_time` ) values( '任务状态', 'task_status', '0', 'admin', sysdate() );
insert into `sys_dict_data`( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `status`, `create_by`, `create_time` ) values( 1, '待巡检', '0', 'task_status', 'default', '0', 'admin', sysdate() );
insert into `sys_dict_data`( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `status`, `create_by`, `create_time` ) values( 2, '巡检中', '1', 'task_status', 'default', '0', 'admin', sysdate() );
insert into `sys_dict_data`( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `status`, `create_by`, `create_time` ) values( 3, '已完结', '2', 'task_status', 'default', '0', 'admin', sysdate() );
insert into `sys_dict_data`( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `status`, `create_by`, `create_time` ) values( 4, '待回传', '3', 'task_status', 'default', '0', 'admin', sysdate() );
insert into `sys_dict_data`( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `status`, `create_by`, `create_time` ) values( 5, '超时', '4', 'task_status', 'default', '0', 'admin', sysdate() );

-- 数据字典 外观情况
insert into `sys_dict_type`( `dict_name`, `dict_type`, `status`, `create_by`, `create_time` ) values( '外观情况', 'impression', '0', 'admin', sysdate() );
insert into `sys_dict_data`( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `status`, `create_by`, `create_time` ) values( 1, '外观完整', '0', 'impression', 'default', '0', 'admin', sysdate() );
insert into `sys_dict_data`( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `status`, `create_by`, `create_time` ) values( 2, '外观破损', '1', 'impression', 'default', '0', 'admin', sysdate() );

-- 数据字典 网络通讯情况
insert into `sys_dict_type`( `dict_name`, `dict_type`, `status`, `create_by`, `create_time` ) values( '网络通讯情况', 'network', '0', 'admin', sysdate() );
insert into `sys_dict_data`( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `status`, `create_by`, `create_time` ) values( 1, '通讯正常', '0', 'network', 'default', '0', 'admin', sysdate() );
insert into `sys_dict_data`( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `status`, `create_by`, `create_time` ) values( 2, '通讯异常', '1', 'network', 'default', '0', 'admin', sysdate() );

-- 数据字典 供配电情况
insert into `sys_dict_type`( `dict_name`, `dict_type`, `status`, `create_by`, `create_time` ) values( '供配电情况', 'power', '0', 'admin', sysdate() );
insert into `sys_dict_data`( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `status`, `create_by`, `create_time` ) values( 1, '配电正常', '0', 'power', 'default', '0', 'admin', sysdate() );
insert into `sys_dict_data`( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `status`, `create_by`, `create_time` ) values( 2, '配电异常', '1', 'power', 'default', '0', 'admin', sysdate() );

-- 数据字典 巡查状态
insert into `sys_dict_type`( `dict_name`, `dict_type`, `status`, `create_by`, `create_time` ) values( '巡查状态', 'xc_status', '0', 'admin', sysdate() );
insert into `sys_dict_data`( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `status`, `create_by`, `create_time` ) values( 1, '未巡检', '0', 'xc_status', 'default', '0', 'admin', sysdate() );
insert into `sys_dict_data`( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `status`, `create_by`, `create_time` ) values( 2, '已巡检', '1', 'xc_status', 'default', '0', 'admin', sysdate() );

-- 故障清单表  sd_fault_list 添加字段
alter table sd_fault_list  add column eq_run_status varchar(10) comment '设备运行状态';

-- 修改sd_fault_list表字段fault_type的字段类型
alter table sd_fault_list modify column fault_type  varchar(10)  comment '故障类型';
-- 修改sd_fault_list表字段fault_level的字段类型
alter table sd_fault_list modify column fault_level  varchar(10)  comment '故障等级';
-- 修改sd_fault_list表字段fallt_remove_statue的字段类型
alter table sd_fault_list modify column fallt_remove_statue  varchar(10)  comment '故障消除状态';
-- 修改sd_fault_list表字段fault_status的字段类型
alter table sd_fault_list modify column fault_status  varchar(10)  comment '故障状态';

-- 修改sd_task_list表字段publish_status的字段类型
alter table sd_task_list modify column publish_status  varchar(10)  comment '任务发布状态';
-- 修改sd_task_list表字段task_status的字段类型
alter table sd_task_list modify column task_status  varchar(10)  comment '任务状态';

-- 修改sd_patrol_list表字段impression的字段类型
alter table sd_patrol_list modify column impression  varchar(10)  comment '外观情况';
-- 修改sd_patrol_list表字段network的字段类型
alter table sd_patrol_list modify column network  varchar(10)  comment '网络通讯情况';
-- 修改sd_patrol_list表字段power的字段类型
alter table sd_patrol_list modify column power  varchar(10)  comment '供配电情况';
-- 修改sd_patrol_list表字段xc_status的字段类型
alter table sd_patrol_list modify column xc_status  varchar(10)  comment '巡查状态';
-- 修改sd_push_history表字段push_data的字段类型
ALTER TABLE `tunnel-jq`.`sd_push_history`
    MODIFY COLUMN `push_data` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '推送数据' AFTER `data_type`;

-- 触发器表添加预警类型字段
alter table sd_trigger  add column warning_type varchar(10) comment '预警类型0：仅预警；1：预警联动';


-- 数据字典 任务发布状态
insert into `sys_dict_type`( `dict_name`, `dict_type`, `status`, `create_by`, `create_time` ) values( '操作类型', 'opt_type', '0', 'admin', sysdate() );
insert into `sys_dict_data`( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `status`, `create_by`, `create_time` ) values( 1, '派单', '0', 'opt_type', 'default', '0', 'admin', sysdate() );
insert into `sys_dict_data`( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `status`, `create_by`, `create_time` ) values( 2, '接收', '1', 'opt_type', 'default', '0', 'admin', sysdate() );
insert into `sys_dict_data`( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `status`, `create_by`, `create_time` ) values( 3, '提交', '2', 'opt_type', 'default', '0', 'admin', sysdate() );


-- 部门表主键修改为varchar（集团部门数据对接需要）  ---------start---------------
-- 部门表sys_dept字段修改
alter table sys_dept DROP  column jt_dept_id ;
alter table sys_dept DROP  column jt_pid;
alter table sys_dept modify column dept_id varchar(50) comment '部门id';
alter table sys_dept modify column parent_id varchar(50) comment '父部门id';

-- 角色部门关联表sys_role_dept字段修改
alter table sys_role_dept modify column dept_id varchar(50) comment '部门ID';

-- 用户表sys_user字段修改
alter table sys_user modify column dept_id varchar(50) comment '部门ID';

-- sd_xfwater_record表字段修改
 alter table sd_xfwater_record modify column dept_id varchar(50) comment '部门ID';

-- 隧道表sd_tunnels字段修改
 alter table sd_tunnels modify column dept_id varchar(50) comment '部门ID';

-- iot_device字段修改
alter table iot_device modify column manage_agency_id varchar(50) comment '管理单位/组织机构表';

-- 部门表主键修改为varchar（集团部门数据对接需要）  ---------end---------------

ALTER TABLE sd_emergency_vehicle
    MODIFY COLUMN `id` bigint(20) NOT NULL COMMENT '主键' FIRST,
    ADD COLUMN `acc_state` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '车辆运行状态 0:未启动,1:启动,2:离线,4:清障救援,5:道路巡查' AFTER `status_desc`;

-- 物联设备厂商表
CREATE TABLE `sd_devices_brand` (
  `supplier_id` varchar(11) DEFAULT NULL COMMENT '设备厂商编号',
  `supplier_name` varchar(100) DEFAULT NULL COMMENT '设备厂商名称',
  `short_name` varchar(100) DEFAULT NULL COMMENT '简称',
  `is_del` tinyint(4) DEFAULT NULL COMMENT '是否删除（1-是，0-否）',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime  DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime  DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='物联设备厂商表';

-- 集团部门表
CREATE TABLE `sys_dept_sg` (
  `id` varchar(36) NOT NULL COMMENT '机构主键',
  `pid` varchar(50) DEFAULT NULL COMMENT '父节点代码',
  `code` varchar(200) DEFAULT NULL COMMENT '编码',
  `p_code` varchar(200) DEFAULT NULL COMMENT '父节点编码',
  `pids` varchar(2100) DEFAULT NULL COMMENT '所有上级ID，用逗号分开',
  `name` varchar(200) DEFAULT NULL COMMENT '机构名称',
  `type` varchar(10) DEFAULT NULL COMMENT '类别',
  `sort` bigint(20) DEFAULT NULL COMMENT '排序字段',
  `spell` varchar(255) DEFAULT NULL COMMENT '机构简拼',
  `has_leaf` tinyint(1) DEFAULT NULL COMMENT '是否有叶子',
  `property` varchar(2) DEFAULT NULL COMMENT '属性(公司 中心 站)等',
  `full_name` varchar(255) DEFAULT NULL COMMENT '全路径名称',
  `creator` varchar(200) DEFAULT NULL COMMENT '创建人',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `updater` varchar(200) DEFAULT NULL COMMENT '修改人',
  `update_date` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='机构表';

-- 集团用户表
CREATE TABLE `sys_user_sg` (
  `id` varchar(50) NOT NULL COMMENT 'id',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `real_name` varchar(50) DEFAULT NULL COMMENT '姓名',
  `type` varchar(20) DEFAULT NULL COMMENT '人员类别',
  `head_url` varchar(200) DEFAULT NULL COMMENT '头像',
  `gender` varchar(2) DEFAULT NULL COMMENT '性别   0：男   1：女    2：保密',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(50) DEFAULT NULL COMMENT '手机号,唯一,验证',
  `mobile2` varchar(50) DEFAULT NULL COMMENT '备用电话',
  `position_desc` varchar(255) DEFAULT NULL COMMENT '职务描述',
  `dept_desc` varchar(1500) DEFAULT NULL COMMENT '所在单位描述',
  `super_admin` tinyint(1) DEFAULT NULL COMMENT '超级管理员   0：否   1：是',
  `has_admin` tinyint(1) DEFAULT NULL COMMENT '是否有分权权限 0：否   1：是',
  `data_scope_type` tinyint(1) DEFAULT NULL COMMENT '数据范围规则1 本单位 2 本节点及以下 3 全部 9跨部门',
  `dept_id` varchar(50) DEFAULT NULL COMMENT '隶属单位id',
  `company_id` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '单位id',
  `id_card` varchar(20) DEFAULT NULL COMMENT '身份证号',
  `superior_id` varchar(50) DEFAULT NULL COMMENT '上级',
  `office_room` varchar(255) DEFAULT NULL COMMENT '办公室',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态  0：停用   1：正常',
  `order_id` bigint(20) DEFAULT NULL COMMENT '排序',
  `im_status` tinyint(4) DEFAULT NULL COMMENT '即时通讯状态',
  `out_user_id` varchar(50) DEFAULT NULL COMMENT '对外对接用户',
  `hr_employee_id` varchar(50) DEFAULT NULL COMMENT '人资雇员id',
  `xdl_user_id` varchar(50) DEFAULT NULL COMMENT '行动力用户账号',
  `has_valid_date` tinyint(1) DEFAULT '0' COMMENT '是否限制使用日期',
  `start_date` datetime DEFAULT NULL COMMENT '用户启用时间',
  `end_date` datetime DEFAULT NULL COMMENT '用户截止时间',
  `spell` varchar(255) DEFAULT NULL COMMENT '简拼',
  `pinyin` varchar(255) DEFAULT NULL COMMENT '全拼音',
  `has_delete` tinyint(1) DEFAULT '0' COMMENT '是否删除',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `updater` varchar(50) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `weather_city` varchar(30) DEFAULT NULL COMMENT '气象地区',
  `password_update_date` datetime DEFAULT NULL COMMENT '口令更新日期(空表示初始口令)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_username` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统用户';

-- 国标公路字典
CREATE TABLE `sd_road` (
  `road_id` char(7) NOT NULL COMMENT '国标路线编码',
  `road_name` varchar(150) DEFAULT NULL COMMENT '国标路线名称',
  `tec_level` int(11) DEFAULT NULL COMMENT '技术等级',
  `start_site` varchar(150) DEFAULT NULL COMMENT '起始计费位置地点',
  `start_stake_num` varchar(20) DEFAULT NULL COMMENT '起始计费位置桩号',
  `start_stake_no` decimal(30,20) DEFAULT NULL COMMENT '起始计费位置桩号（数字）',
  `start_lat` varchar(20) DEFAULT NULL COMMENT '起始计费位置纬度',
  `start_lng` varchar(20) DEFAULT NULL COMMENT '起始计费位置经度',
  `start_station_id` varchar(20) DEFAULT NULL COMMENT '起点收费站编号',
  `end_site` varchar(150) DEFAULT NULL COMMENT '终止计费位置地点',
  `end_stake_num` varchar(20) DEFAULT NULL COMMENT '终止计费位置桩号',
  `end_stake_no` decimal(30,20) DEFAULT NULL COMMENT '终止计费位置桩号（数字）',
  `end_lat` varchar(20) DEFAULT NULL COMMENT '终止计费位置纬度',
  `end_lng` varchar(20) DEFAULT NULL COMMENT '终止计费位置经度',
  `end_station_id` varchar(20) DEFAULT NULL COMMENT '终止收费站编号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `effect_time` datetime DEFAULT NULL COMMENT '生效时间',
  `expired_time` datetime DEFAULT NULL COMMENT '失效时间',
  PRIMARY KEY (`road_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='国标公路字典';

-- 高速公路路段表
CREATE TABLE `sd_section` (
  `section_id` varchar(11) NOT NULL COMMENT '路段编码',
  `section_name` varchar(100) NOT NULL COMMENT '路段名称',
  `road_id` varchar(7) DEFAULT NULL COMMENT '路线编码',
  `road_name` varchar(100) DEFAULT NULL COMMENT '路线名称',
  `region_id` varchar(6) DEFAULT NULL COMMENT '所在地市编码',
  `region_name` varchar(10) DEFAULT NULL COMMENT '所在地市名称',
  `start_stake_num` varchar(10) DEFAULT NULL COMMENT '起点桩号',
  `end_stake_num` varchar(10) DEFAULT NULL COMMENT '终点桩号',
  `start_stake_no` double DEFAULT NULL COMMENT '开始桩号（数字）',
  `end_stake_no` double DEFAULT NULL COMMENT '结束桩号（数字）',
  `section_length` decimal(10,3) DEFAULT NULL COMMENT '路段长度，单位米',
  `opma_clique_id` varchar(20) DEFAULT NULL COMMENT '所属集团公司编码',
  `opma_clique_name` varchar(60) DEFAULT NULL COMMENT '所属集团公司名称',
  `opma_manager_corp_id` varchar(20) DEFAULT NULL COMMENT '管养公司编码',
  `opma_manager_corp_name` varchar(60) DEFAULT NULL COMMENT '管养公司名称',
  `opma_manager_id` varchar(20) DEFAULT NULL COMMENT '管养单位编码',
  `opma_manager_name` varchar(60) DEFAULT NULL COMMENT '管养单位名称',
  `opma_xxsubcenter_id` varchar(20) DEFAULT NULL COMMENT '信息分中心编码',
  `opma_xxsubcenter_name` varchar(60) DEFAULT NULL COMMENT '信息分中心名称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `effect_time` datetime NOT NULL COMMENT '生效时间',
  `expired_time` datetime DEFAULT NULL COMMENT '失效时间',
  PRIMARY KEY (`section_id`,`effect_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='高速公路路段表，该表基于智慧管理中心白皮书，并按照路线、地市划分';

-- 高速公路子路段表
CREATE TABLE `sd_sub_section` (
  `sub_section_id` varchar(11) NOT NULL COMMENT '子路段编码',
  `section_id` varchar(11) DEFAULT NULL COMMENT '路段编码',
  `section_name` varchar(100) DEFAULT NULL COMMENT '路段名称',
  `road_id` varchar(7) DEFAULT NULL COMMENT '路线编码',
  `road_name` varchar(100) DEFAULT NULL COMMENT '路线名称',
  `region_id` varchar(6) DEFAULT NULL COMMENT '所在地市编码',
  `region_name` varchar(10) DEFAULT NULL COMMENT '所在地市名称',
  `start_stake_no` decimal(20,3) DEFAULT NULL COMMENT '起点桩号（数字）',
  `start_stake_num` varchar(10) DEFAULT NULL COMMENT '起点桩号',
  `end_stake_no` decimal(20,3) DEFAULT NULL COMMENT '终点桩号（数字）',
  `end_stake_num` varchar(10) DEFAULT NULL COMMENT '终点桩号',
  `section_length` decimal(10,3) DEFAULT NULL COMMENT '路段长度，单位米',
  `opma_manager_id` varchar(7) DEFAULT NULL COMMENT '管养单位编码',
  `opma_manager_name` varchar(100) DEFAULT NULL COMMENT '管养单位名称',
  `opma_manager_corp_id` varchar(5) DEFAULT NULL COMMENT '管养公司编码',
  `opma_manager_corp_name` varchar(100) DEFAULT NULL COMMENT '管养公司名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `effect_time` datetime DEFAULT NULL COMMENT '生效时间',
  `expired_time` datetime DEFAULT NULL COMMENT '失效时间',
  `stat_flag` int(11) DEFAULT NULL COMMENT '统计标志 0:不纳入统计范围 1:纳入统计范围',
  PRIMARY KEY (`sub_section_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='高速公路子路段表';

-- 设备表添加字段：
alter table sd_devices add column sn varchar(255) comment '设备唯一标识码' ;
alter table sd_devices add column external_device_id varchar(255) comment '外部设备ID' ;

-- 隧道关联关系表：
CREATE TABLE `tunnel_association` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tunnel_id` varchar(255) DEFAULT NULL COMMENT '隧道id',
  `tunnel_direction` varchar(255) DEFAULT NULL COMMENT '隧道方向',
  `external_system_id` varchar(255) DEFAULT NULL COMMENT '外部系统ID',
  `external_system_tunnel_id` varchar(255) DEFAULT NULL COMMENT '外部系统隧道ID',
`external_system_tunnel_direction` varchar(255) DEFAULT NULL COMMENT '外部系统隧道方向',
`remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=140 DEFAULT CHARSET=utf8mb4 COMMENT='隧道关联关系表';

-- 外部系统表：
CREATE TABLE `external_system` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `brand_id` varchar(255) DEFAULT NULL COMMENT '厂商\品牌id',
  `is_direction` varchar(255) DEFAULT NULL COMMENT '是否映射方向（0：是，1：否）',
  `username` varchar(255) DEFAULT NULL COMMENT '用户名',
`password` varchar(255) DEFAULT NULL COMMENT '密码',
`network_status` varchar(255) DEFAULT NULL COMMENT '网络状态',
`system_name` varchar(255) DEFAULT NULL COMMENT '系统名称',
`system_url` varchar(255) DEFAULT NULL COMMENT '系统地址',
`remark` varchar(255) DEFAULT NULL COMMENT '备注',
`create_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
`update_by` varchar(255) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改日期',
	PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=140 DEFAULT CHARSET=utf8mb3 COMMENT='外部系统表';


-- 隧道方向修改：原有隧道方向（上行1、下行0） 修改为跟集团隧道方向保持一致（上行1，下行2）
-- 设备表--方向字段修改
update sd_devices set eq_direction = '2' where eq_direction = '0'
-- 字典值：设备方向 sd_direction 修改

-- 添加车辆类型表
CREATE TABLE `sd_vehicle_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `vehicle_type_code` varchar(50) DEFAULT NULL COMMENT '车辆类型编码',
  `vehicle_type_name` varchar(50) DEFAULT NULL COMMENT '车辆类型名称',
  `is_key_vehicle` varchar(1) DEFAULT '0' COMMENT '是否是重点车辆 0:否 1:是  默认为0',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8 COMMENT='车辆类型配置表';

-- 新增 设备协议配置表：
CREATE TABLE `sd_devices_protocol` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `brand_id` varchar(50) DEFAULT NULL COMMENT '厂商 ID',
  `eq_type_id` bigint(20) DEFAULT NULL COMMENT '设备类型',
  `protocol_name` varchar(255) DEFAULT NULL COMMENT '协议名称',
  `protocol_type` varchar(255) DEFAULT NULL COMMENT '协议类型',
  `class_name` varchar(255) DEFAULT NULL COMMENT '类名',
  `is_del` tinyint(4) DEFAULT NULL COMMENT '是否删除（1-是，0-否）',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '修改者',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `note` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='设备协议配置表';

-- 微波车检：新增表 sd_microwave_periodic_statistics、sd_microwave_real_data

-- 设备表添加 设备大类、外部系统ID字段
alter table sd_devices modify column brand_id varchar(30) comment '品牌ID';
alter table sd_devices add column f_eq_type varchar(100) comment '设备大类' after eq_type;
alter table sd_devices add column external_system_id int(11) comment '外部系统ID';

-- 外部系统表添加 所属隧道ID字段
alter table external_system add column tunnel_id varchar(100) comment '所属隧道 ID' after brand_id;

-- 新增协议类型字典
INSERT INTO sys_dict_type(dict_id, dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark) VALUES (107, '协议类型', 'device_protocol_type', '0', 'admin', '2022-12-07 17:06:13', '', NULL, NULL);
INSERT INTO sys_dict_data(dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark) VALUES (511, 1, 'UDP', '1', 'device_protocol_type', NULL, 'default', 'N', '0', 'admin', '2022-12-07 17:06:50', '', NULL, NULL);
INSERT INTO sys_dict_data(dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark) VALUES (512, 2, 'TCP', '2', 'device_protocol_type', NULL, 'default', 'N', '0', 'admin', '2022-12-07 17:07:11', '', NULL, NULL);
INSERT INTO sys_dict_data(dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark) VALUES (513, 3, 'HTTP', '3', 'device_protocol_type', NULL, 'default', 'N', '0', 'admin', '2022-12-07 17:07:22', '', NULL, NULL);


CREATE TABLE `sd_plan_flow` (
                                `id` bigint(20) NOT NULL COMMENT 'id',
                                `pid` bigint(20) DEFAULT NULL COMMENT 'pid父级',
                                `flow_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '环节名称',
                                `sort` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '排序',
                                `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
                                `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                `update_by` varchar(64) DEFAULT NULL COMMENT '修改者',
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='事件环节表';

-- 图片信息表新增img_type字段
ALTER TABLE sd_traffic_image`
    ADD COLUMN `img_type` varchar(1) NULL COMMENT '文件类型0：图片、1：视频' AFTER `business_id`;

-- 同步历史记录表修改push_data字段
ALTER TABLE `sd_push_history`
    MODIFY COLUMN `push_data` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '推送数据' AFTER `data_type`;

-- 事件管理表新增stake_end_num字段
ALTER TABLE sd_event`
    ADD COLUMN `stake_end_num` varchar(50) NULL COMMENT '事件终点桩号' AFTER `stake_num`;

-- 新增事件类型预案流程关联表
CREATE TABLE `sd_join_type_flow`  (
                                      `id` bigint(20) NOT NULL COMMENT 'id',
                                      `event_type_id` bigint(20) NULL COMMENT '事件类型id',
                                      `flow_id` bigint(20) NULL COMMENT '环节id',
                                      `flow_pid` bigint(20) NULL DEFAULT NULL COMMENT '环节pid',
                                      `flow_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '环节名称',
                                      `flow_sort` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '环节排序',
                                      `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                      `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                                      PRIMARY KEY (`id`)
) COMMENT = '事件类型预案流程关联表';

CREATE TABLE `sd_event_handle` (
                                   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                                   `event_id` bigint(20) DEFAULT NULL COMMENT '事件id',
                                   `flow_id` bigint(20) DEFAULT NULL COMMENT '流程id',
                                   `flow_pid` bigint(20) DEFAULT NULL COMMENT '流程pid',
                                   `flow_content` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '事件流程内容',
                                   `event_state` varchar(1) DEFAULT '0' COMMENT '事件状态 0:未完成 1:已完成',
                                   `plan_id` bigint(20) DEFAULT NULL COMMENT '预案id',
                                   `strategy_id` bigint(20) DEFAULT NULL COMMENT '策略id',
                                   `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                   `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                   PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=188 DEFAULT CHARSET=latin1 COMMENT='事件处置信息表';

-- 事件类型表新增字段
ALTER TABLE `sd_event_type`
    ADD COLUMN `is_usable` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 1 COMMENT '是否可用 0：禁用 1：启用' AFTER `icon_url`;

-- 设备故障表新增字段
ALTER TABLE `sd_fault_list`
    ADD COLUMN `fault_escalation_type` varchar(1) NULL DEFAULT 0 COMMENT '上报区分 0：人为填报 1：设备上报' AFTER `eq_run_status`;

-- 新增车道统计数据表
CREATE TABLE `sd_lane_statistics` (
      `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
      `tunnel_id` varchar(100) DEFAULT NULL COMMENT '隧道id',
      `lane_no` int(20) DEFAULT NULL COMMENT '车道编号',
      `speed` varchar(20) DEFAULT NULL COMMENT '平均速度 单位km/h',
      `time_occupy` varchar(20) DEFAULT NULL COMMENT '时间占有率',
      `space_occupy` varchar(20) DEFAULT NULL COMMENT '空间占有率',
      `gap` varchar(20) DEFAULT NULL COMMENT '车头间距 单位米',
      `gap_time` varchar(20) DEFAULT NULL COMMENT '车头时距 单位秒',
      `time` datetime DEFAULT NULL COMMENT '时间戳',
      `flow_small` int(20) DEFAULT NULL COMMENT '小车流量',
      `flow_medium` int(20) DEFAULT NULL COMMENT '中车流量',
      `flow_large` int(20) DEFAULT NULL COMMENT '大车流量',
      `flowx_large` int(20) DEFAULT NULL COMMENT '超大车流量',
      `cars` int(20) DEFAULT NULL COMMENT '车道车辆数',
      `start_time` datetime DEFAULT NULL COMMENT '统计开始时间',
      `end_time` datetime DEFAULT NULL COMMENT '统计结束时间',
      `road_dir` varchar(1) DEFAULT NULL COMMENT '上下行标志 1：上行，2：下行',
      `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
      `create_time` datetime DEFAULT NULL COMMENT '创建时间',
      `update_by` varchar(64) DEFAULT NULL COMMENT '修改者',
      `update_time` datetime DEFAULT NULL COMMENT '修改时间',
      PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='车道统计数据表';

-- 新增路段统计数据表
CREATE TABLE `sd_road_section_statistics` (
      `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
      `tunnel_id` varchar(100) DEFAULT NULL COMMENT '隧道id',
      `speed` varchar(20) DEFAULT NULL COMMENT '平均速度 路段平均速度，单位km/h',
      `time_occupy` varchar(20) DEFAULT NULL COMMENT '时间占有率',
      `space_occupy` varchar(20) DEFAULT NULL COMMENT '空间占有率,路段车辆占道路比例',
      `gap` varchar(20) DEFAULT NULL COMMENT '平均车头间距 单位米',
      `gap_time` varchar(20) DEFAULT NULL COMMENT '平均车头时距 单位秒',
      `time` datetime DEFAULT NULL COMMENT '时间戳',
      `in_flow` varchar(20) DEFAULT NULL COMMENT '进入流量',
      `out_flow` varchar(20) DEFAULT NULL COMMENT '离开流量',
      `congestion_index` varchar(20) DEFAULT NULL COMMENT '拥堵指标',
      `road_dir` varchar(1) DEFAULT NULL COMMENT '上下行标志 1：上行，2：下行',
      `saturation_vc` varchar(20) DEFAULT NULL COMMENT '饱和度',
      `start_time` datetime DEFAULT NULL COMMENT '统计开始时间',
      `end_time` datetime DEFAULT NULL COMMENT '统计结束时间',
      `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
      `create_time` datetime DEFAULT NULL COMMENT '创建时间',
      `update_by` varchar(64) DEFAULT NULL COMMENT '修改者',
      `update_time` datetime DEFAULT NULL COMMENT '修改时间',
      PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='路段统计数据表';

-- 新增路段车辆行驶记录表
CREATE TABLE `sd_vehicle_driving` (
      `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
      `tunnel_id` varchar(100) DEFAULT NULL COMMENT '隧道id',
      `track_id` bigint(20) DEFAULT NULL COMMENT '轨迹ID',
      `plate_color` varchar(10) DEFAULT NULL COMMENT '车牌颜色',
      `plate_number` varchar(15) DEFAULT NULL COMMENT '车牌号',
      `object_type` varchar(2) DEFAULT NULL COMMENT '目标类型',
      `vehicle_type` varchar(10) DEFAULT NULL COMMENT '车辆类型',
      `vehicle_color` varchar(10) DEFAULT NULL COMMENT '车身颜色',
      `speed` varchar(20) DEFAULT NULL COMMENT '车辆速度，单位：km/h',
      `travel_type` varchar(1) DEFAULT NULL COMMENT '进入或者离开标志 1：进入2：离开',
      `start_time` datetime DEFAULT NULL COMMENT '进入或离开的时间',
      `road_dir` varchar(1) DEFAULT NULL COMMENT '上下行标志 1：上行，2：下行',
      `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
      `create_time` datetime DEFAULT NULL COMMENT '创建时间',
      `update_by` varchar(64) DEFAULT NULL COMMENT '修改者',
      `update_time` datetime DEFAULT NULL COMMENT '修改时间',
      PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='路段车辆行驶记录';

-- 雷达监测感知数据表新增字段
ALTER TABLE `sd_radar_detect_data`
    MODIFY COLUMN `record_Serial_Number` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流水号' AFTER `detect_time`,
    ADD COLUMN `object_type` varchar(2) NULL COMMENT '目标类型' AFTER `distance`,
    ADD COLUMN `road_dir` varchar(1) NULL COMMENT '上下行标志 1：上行，2：下行' AFTER `object_type`;

-- 路段统计数据表新增字段
ALTER TABLE `tunnel-jq`.`sd_road_section_statistics`
    CHANGE COLUMN `start_time` `cars` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '路段当前车辆数' AFTER `saturation_vc`;

-- 雷达监测感知数据表新增字段
ALTER TABLE `tunnel-jq`.`sd_radar_detect_data`
    ADD COLUMN `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间' AFTER `road_dir`;
