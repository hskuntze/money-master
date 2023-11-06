INSERT INTO tb_role (authority) VALUES ('ROLE_ADMIN');
INSERT INTO tb_role (authority) VALUES ('ROLE_USER');

INSERT INTO tb_user (name, email, password, phone_number, id_number, id_type, birth, enabled, gender, address_line, number, district, city, state, zip_code, country, additional_details, address_type) VALUES ('Hassan Kuntze Rodrigues da Cunha', 'hskuntze@gmail.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG', '61992515369', 1901942198, 1, '1999-03-03', true, 1, 'SQS 411 Bloco F', '212', 'Asa Sul', 'Brasília', 'Distrito Federal', '70277060', 'Brasil', null, 1);
INSERT INTO tb_user (name, email, password, phone_number, id_number, id_type, birth, enabled, gender, address_line, number, district, city, state, zip_code, country, additional_details, address_type) VALUES ('Alobiano Hanai Ilkanis', 'email@email.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG', '61992515369', 13886887000109, 2, '2014-01-01', true, 1, 'Rua Sólon Ubarana', 's/n', 'Centro', 'Monte Alegre', 'Rio Grande do Norte', '59182974', 'Brasil', 'Rua sem número. Necessário gritar ao chegar.', 2);

INSERT INTO tb_vault (savings, on_wallet, allowed_to_spend, user_id) VALUES (0.0, 0.0, 0.0, 1);
INSERT INTO tb_vault (savings, on_wallet, allowed_to_spend, user_id) VALUES (1.0, 1.0, 1.0, 2);

INSERT INTO tb_user_role (user_id, role_id) VALUES (1, 1);
INSERT INTO tb_user_role (user_id, role_id) VALUES (2, 2);

INSERT INTO tb_wishlist (created, description, enabled, installment, installments_value, title, to_buy_at, total_installments, total_value, user_id) VALUES ('2023-10-30', 'Os melhores notebooks', true, false, 2917.59, 'Notebooks', '2024-02-19', 1, 2917.59, 1);

INSERT INTO tb_item (name, price, link, image, variation, wishlist_id) VALUES ('Lenovo Ideapad 3', 2250.99, 'https://www.amazon.com.br/Notebook-Lenovo-i3-1115G4-Graphics-82MD000ABR/dp/B09ZKCVTFR/ref=sr_1_1_sspa?__mk_pt_BR=%C3%85M%C3%85%C5%BD%C3%95%C3%91&crid=N2SXSY33O0EU&keywords=ideapad+3&qid=1698702322&sprefix=ideapad+3%2Caps%2C280&sr=8-1-spons&ufe=app_do%3Aamzn1.fos.25548f35-0de7-44b3-b28e-0f56f3f96147&sp_csd=d2lkZ2V0TmFtZT1zcF9hdGY&psc=1', 'https://m.media-amazon.com/images/I/61PrCgQ2KwL._AC_SL1500_.jpg', 0.0, 1);
INSERT INTO tb_item (name, price, link, image, variation, wishlist_id) VALUES ('Lenovo Ideapad 3i', 2250.99, 'https://www.amazon.com.br/Notebook-Lenovo-IdeaPad-i5-1135G7-82MD0007BR/dp/B0B35BGG1P/ref=sr_1_1?__mk_pt_BR=%C3%85M%C3%85%C5%BD%C3%95%C3%91&crid=3JA0N3D1D8663&keywords=ideapad+3i&qid=1698702369&sprefix=ideapad+3%2Caps%2C224&sr=8-1&ufe=app_do%3Aamzn1.fos.25548f35-0de7-44b3-b28e-0f56f3f96147', 'https://m.media-amazon.com/images/I/61PrCgQ2KwL._AC_SL1500_.jpg', 0.0, 1);

INSERT INTO tb_item_history (fluctuation, item_id) VALUES (0.0, 1);

INSERT INTO tb_item_price (date, price, item_history_id) VALUES ('2023-10-30', 100.0, 1);
INSERT INTO tb_item_price (date, price, item_history_id) VALUES ('2023-11-02', 80.0, 1);
