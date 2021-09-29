

INSERT INTO "public"."auth_user"("user_id", "username", "password") VALUES (1, 'admin', '$2a$05$W53Pt8ga6lrQh4D7lrx9V.E0cXS0smuhDAW6x8VyjAOYJDyZntvWi');
INSERT INTO "public"."auth_user"("user_id", "username", "password") VALUES (2, 'test', '$2a$05$gKhEcrHpyE1v6wKeNX/GFeymVPuI6aSrqJfU/cUPqTffmBiqRFlJC');
INSERT INTO "public"."auth_user"("user_id", "username", "password") VALUES (3, 'test_a', '$2a$05$SCOhxjeVdeEh27Zxfo4mW.RJrqc0eE9NB8bYEmchC6lpOs/MJ8Jti');

INSERT INTO "public"."auth_role"("role_id", "role_name") VALUES (1, 'role_admin');
INSERT INTO "public"."auth_role"("role_id", "role_name") VALUES (2, 'role_test');

INSERT INTO "public"."auth_user_role"("user_id", "role_id") VALUES (1, 1);
INSERT INTO "public"."auth_user_role"("user_id", "role_id") VALUES (2, 2);
INSERT INTO "public"."auth_user_role"("user_id", "role_id") VALUES (3, 2);

INSERT INTO "public"."auth_uri"("uri_id", "uri_path") VALUES (1, '/test/**');
INSERT INTO "public"."auth_uri"("uri_id", "uri_path") VALUES (2, '/test/example/*');
INSERT INTO "public"."auth_uri"("uri_id", "uri_path") VALUES (3, '/test/example/page');
INSERT INTO "public"."auth_uri"("uri_id", "uri_path") VALUES (4, '/test/example/insert');
INSERT INTO "public"."auth_uri"("uri_id", "uri_path") VALUES (5, '/test/example/update');

INSERT INTO "public"."auth_res"("res_id", "res_name", "res_parent_id", "res_type", "menu_path") VALUES (1, '测试目录01', 0, 'd', NULL);
INSERT INTO "public"."auth_res"("res_id", "res_name", "res_parent_id", "res_type", "menu_path") VALUES (2, '测试菜单11', 1, 'm', '/test/Example11');
INSERT INTO "public"."auth_res"("res_id", "res_name", "res_parent_id", "res_type", "menu_path") VALUES (3, '测试菜单12', 1, 'm', '/test/Example12');
INSERT INTO "public"."auth_res"("res_id", "res_name", "res_parent_id", "res_type", "menu_path") VALUES (4, '查询操作', 2, 'o', NULL);
INSERT INTO "public"."auth_res"("res_id", "res_name", "res_parent_id", "res_type", "menu_path") VALUES (5, '变更操作', 2, 'o', NULL);
INSERT INTO "public"."auth_res"("res_id", "res_name", "res_parent_id", "res_type", "menu_path") VALUES (6, '测试目录02', 0, 'd', NULL);
INSERT INTO "public"."auth_res"("res_id", "res_name", "res_parent_id", "res_type", "menu_path") VALUES (7, '测试菜单21', 6, 'm', '/test/Example21');
INSERT INTO "public"."auth_res"("res_id", "res_name", "res_parent_id", "res_type", "menu_path") VALUES (8, '测试菜单22', 6, 'm', '/test/Example22');

INSERT INTO "public"."auth_res_uri"("res_id", "uri_id") VALUES (3, 3);
INSERT INTO "public"."auth_res_uri"("res_id", "uri_id") VALUES (4, 4);
INSERT INTO "public"."auth_res_uri"("res_id", "uri_id") VALUES (4, 5);

INSERT INTO "public"."auth_role_res"("role_id", "res_id") VALUES (2, 1);
INSERT INTO "public"."auth_role_res"("role_id", "res_id") VALUES (2, 2);
INSERT INTO "public"."auth_role_res"("role_id", "res_id") VALUES (2, 3);
INSERT INTO "public"."auth_role_res"("role_id", "res_id") VALUES (2, 4);
INSERT INTO "public"."auth_role_res"("role_id", "res_id") VALUES (2, 5);
INSERT INTO "public"."auth_role_res"("role_id", "res_id") VALUES (2, 6);
INSERT INTO "public"."auth_role_res"("role_id", "res_id") VALUES (2, 7);
INSERT INTO "public"."auth_role_res"("role_id", "res_id") VALUES (2, 8);



