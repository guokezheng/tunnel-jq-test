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
alter table sd_tunnels add column start_pile varchar(20) comment '隧道开始桩号（整型）';
alter table sd_tunnels add column end_pile varchar(20) comment '隧道结束桩号（整型）';

-- 修改sd_equipment_state_icon_file表字段url的字段类型
alter table sd_equipment_state_icon_file modify column url longtext comment '图标地址';

-- 添加字典值：sd_warning_type_judge  预警类型判断符
INSERT INTO sys_dict_type(dict_id, dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark) VALUES (84, '预警类型判断符', 'sd_warning_type_judge', '0', 'admin', '2022-10-17 11:34:02', 'admin', '2022-10-17 11:59:44', '预警类型判断符列表');
INSERT INTO sys_dict_data(dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark) VALUES (405, 0, '大于', '0', 'sd_warning_type_judge', NULL, 'default', 'N', '0', 'admin', '2022-10-17 11:37:55', '', NULL, '大于');
INSERT INTO sys_dict_data(dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark) VALUES (406, 1, '小于', '1', 'sd_warning_type_judge', NULL, 'default', 'N', '0', 'admin', '2022-10-17 11:38:17', '', NULL, '小于');
INSERT INTO sys_dict_data(dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark) VALUES (407, 2, '等于', '2', 'sd_warning_type_judge', NULL, 'default', 'N', '0', 'admin', '2022-10-17 11:38:33', '', NULL, '等于');


-- 修改sd_strategy_rl表字段control_time的字段类型
alter table sd_strategy_rl modify column control_time varchar(20) comment '控制时间';


-- 字典值：策略类型 sd_strategy_type 添加数据：分时控制

INSERT INTO sys_dict_data(dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark) VALUES (383, 4, '分时控制', '3', 'sd_strategy_type', NULL, 'default', 'N', '0', 'admin', '2022-09-12 09:58:31', '', NULL, NULL);


