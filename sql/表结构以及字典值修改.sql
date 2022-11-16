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
  `task_status` int(2) NULL DEFAULT NULL COMMENT '任务状态（0::待巡检、1:巡检中、2:已完结、3:待回传、4:已超时）',
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
alter table sd_vms_template add column category varchar(100) comment '模板类别';

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

-- 修改sd_traffic_image表字段img_url的字段类型
alter table sd_traffic_image modify column img_url longtext comment '图片url';

-- 修改sd_task_liat表字段task_cxtime的字段类型
alter table sd_task_liat column task_cxtime varchar(25) comment '任务持续时间';

-- 修改sd_fault_liat表字段fault_cxtime的字段类型
alter table sd_fault_liat column fault_cxtime varchar(25) comment '故障持续时间（根据当前时间与故障发现时间计算时间差，单位：天、小时；计算时间差，不保存只展示；记录变为“已消除”状态时，保存持续时间）';

-- 数据字典新增设备模拟控制开关标识
INSERT INTO `sys_dict_type`(`dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('设备控制模拟值', 'sys_analog_control_isopen', '0', 'admin', '2022-11-14 11:01:31', '', NULL, NULL);
INSERT INTO `sys_dict_data`(`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (0, '是否开启设备模拟控制', '1', 'sys_analog_control_isopen', NULL, 'default', 'N', '0', 'admin', '2022-11-14 11:44:13', '', NULL, '1：开启模拟控制；0：关闭模拟控制');

-- 事件类型
alter table sd_event_type DROP COLUMN f_id;
alter table sd_event_type add column icon_url longtext comment '图标base64';
alter table sd_event_type add column prev_control_type varchar(20) comment '防控类型';
