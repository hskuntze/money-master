INSERT INTO tb_role (authority) VALUES ('ROLE_ADMIN');
INSERT INTO tb_role (authority) VALUES ('ROLE_USER');

INSERT INTO tb_user (name, email, password, phone_number, id_number, id_type, birth, enabled, gender, address_line, number, district, city, state, zip_code, country, additional_details, address_type) VALUES ('Hassan Kuntze Rodrigues da Cunha', 'hskuntze@gmail.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG', '61992515369', 1901942198, 1, '1999-03-03', true, 1, 'SQS 411 Bloco F', '212', 'Asa Sul', 'Brasília', 'Distrito Federal', '70277060', 'Brasil', null, 1);
INSERT INTO tb_user (name, email, password, phone_number, id_number, id_type, birth, enabled, gender, address_line, number, district, city, state, zip_code, country, additional_details, address_type) VALUES ('Alobiano Hanai Ilkanis', 'email@email.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG', '61992515369', 13886887000109, 2, '2014-01-01', true, 1, 'Rua Sólon Ubarana', 's/n', 'Centro', 'Monte Alegre', 'Rio Grande do Norte', '59182974', 'Brasil', 'Rua sem número. Necessário gritar ao chegar.', 2);

INSERT INTO tb_vault (savings, on_wallet, allowed_to_spend, user_id) VALUES (0.0, 0.0, 0.0, 1);
INSERT INTO tb_vault (savings, on_wallet, allowed_to_spend, user_id) VALUES (1.0, 1.0, 1.0, 2);

INSERT INTO tb_user_role (user_id, role_id) VALUES (1, 1);
INSERT INTO tb_user_role (user_id, role_id) VALUES (2, 2);
