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


--删除sd_devices_copy表

--自动触发定时任务
INSERT INTO `sys_job` VALUES ('自动触发任务', 'DEFAULT', 'strategyTask.triggerJob()', '0 0/5 * * * ?', '1', '0', '0', '', '2022-10-27 10:39:29', '', NULL, '');

--隧道表桩号字段
alter table sd_tunnels add column start_pile_num varchar(20) comment '隧道开始桩号（整型）';
alter table sd_tunnels add column end_pile_num varchar(20) comment '隧道结束桩号（整型）';

--隧道分区表桩号字段
alter table sd_tunnel_subarea add column start_pile varchar(20) comment '开始桩号';
alter table sd_tunnel_subarea add column end_pile varchar(20) comment '结束桩号';

--事件类型sd_event_type
alter table sd_event_type add column f_id varchar(20) comment '开始桩号';
alter table sd_event_type add column simplify_name varchar(20) comment '结束桩号';