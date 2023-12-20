INSERT INTO tb_role (authority) VALUES ('ROLE_ADMIN');
INSERT INTO tb_role (authority) VALUES ('ROLE_USER');

INSERT INTO tb_user (name, email, password, phone_number, id_number, id_type, birth, enabled, gender, address_line, number, district, city, state, zip_code, country, additional_details, address_type, registration_completed) VALUES ('Hassan Kuntze Rodrigues da Cunha', 'hskuntze@gmail.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG', '61992515369', 1901942198, 1, '1999-03-03', true, 1, 'SQS 411 Bloco F', '212', 'Asa Sul', 'Brasília', 'Distrito Federal', '70277060', 'Brasil', null, 1, true);

INSERT INTO tb_vault (savings, on_wallet, allowed_to_spend, user_id) VALUES (0.0, 0.0, 0.0, 1);

INSERT INTO tb_user_role (user_id, role_id) VALUES (1, 1);

INSERT INTO tb_wishlist (created, description, enabled, installment, installments_value, title, to_buy_at, total_installments, total_value, user_id) VALUES ('2023-10-30', 'Os melhores notebooks', true, false, 2917.59, 'Notebooks', '2024-02-19', 1, 2917.59, 1);
INSERT INTO tb_wishlist (created, description, enabled, installment, installments_value, title, to_buy_at, total_installments, total_value, user_id) VALUES ('2023-10-30', 'Os melhores notebooks', true, false, 2917.59, 'Alopatia', '2024-02-19', 1, 2917.59, 1);
INSERT INTO tb_wishlist (created, description, enabled, installment, installments_value, title, to_buy_at, total_installments, total_value, user_id) VALUES ('2023-10-30', 'Os melhores notebooks', true, false, 2917.59, 'Medalhas de Honra', '2024-02-19', 1, 2917.59, 1);
INSERT INTO tb_wishlist (created, description, enabled, installment, installments_value, title, to_buy_at, total_installments, total_value, user_id) VALUES ('2023-10-30', 'Os melhores notebooks', true, false, 2917.59, 'Ferramentas para a casa nova', '2024-02-19', 1, 2917.59, 1);
INSERT INTO tb_wishlist (created, description, enabled, installment, installments_value, title, to_buy_at, total_installments, total_value, user_id) VALUES ('2023-10-30', 'Os melhores notebooks', true, false, 2917.59, 'Móveis para a casa nova', '2024-02-19', 1, 2917.59, 1);
INSERT INTO tb_wishlist (created, description, enabled, installment, installments_value, title, to_buy_at, total_installments, total_value, user_id) VALUES ('2023-10-30', 'Os melhores notebooks', true, false, 2917.59, 'Livros', '2024-02-19', 1, 2917.59, 1);
INSERT INTO tb_wishlist (created, description, enabled, installment, installments_value, title, to_buy_at, total_installments, total_value, user_id) VALUES ('2023-10-30', 'Os melhores notebooks', true, false, 2917.59, 'Roupas', '2024-02-19', 1, 2917.59, 1);
INSERT INTO tb_wishlist (created, description, enabled, installment, installments_value, title, to_buy_at, total_installments, total_value, user_id) VALUES ('2023-10-30', 'Os melhores notebooks', true, false, 2917.59, 'Roupas estilosas', '2024-02-19', 1, 2917.59, 1);
INSERT INTO tb_wishlist (created, description, enabled, installment, installments_value, title, to_buy_at, total_installments, total_value, user_id) VALUES ('2023-10-30', 'Os melhores notebooks', true, false, 2917.59, 'Broches', '2024-02-19', 1, 2917.59, 1);
INSERT INTO tb_wishlist (created, description, enabled, installment, installments_value, title, to_buy_at, total_installments, total_value, user_id) VALUES ('2023-10-30', 'Os melhores notebooks', true, false, 2917.59, 'Material escolar', '2024-02-19', 1, 2917.59, 1);
INSERT INTO tb_wishlist (created, description, enabled, installment, installments_value, title, to_buy_at, total_installments, total_value, user_id) VALUES ('2023-10-30', 'Os melhores notebooks', true, false, 2917.59, 'Luzes para o quarto', '2024-02-19', 1, 2917.59, 1);
INSERT INTO tb_wishlist (created, description, enabled, installment, installments_value, title, to_buy_at, total_installments, total_value, user_id) VALUES ('2023-10-30', 'Os melhores notebooks', true, false, 2917.59, 'À minha roula', '2024-02-19', 1, 2917.59, 1);
INSERT INTO tb_wishlist (created, description, enabled, installment, installments_value, title, to_buy_at, total_installments, total_value, user_id) VALUES ('2023-10-30', 'Os melhores notebooks', true, false, 2917.59, 'Monetários endividados', '2024-02-19', 1, 2917.59, 1);
INSERT INTO tb_wishlist (created, description, enabled, installment, installments_value, title, to_buy_at, total_installments, total_value, user_id) VALUES ('2023-10-30', 'Os melhores notebooks', true, false, 2917.59, 'Notebooks 2', '2024-02-19', 1, 2917.59, 1);

INSERT INTO tb_item (name, price, link, image, wishlist_id, source_platform) VALUES ('Lenovo Ideapad 3', 2250.99, 'https://www.amazon.com.br/Notebook-Lenovo-i3-1115G4-Graphics-82MD000ABR/dp/B09ZKCVTFR/ref=sr_1_1_sspa?__mk_pt_BR=%C3%85M%C3%85%C5%BD%C3%95%C3%91&crid=N2SXSY33O0EU&keywords=ideapad+3&qid=1698702322&sprefix=ideapad+3%2Caps%2C280&sr=8-1-spons&ufe=app_do%3Aamzn1.fos.25548f35-0de7-44b3-b28e-0f56f3f96147&sp_csd=d2lkZ2V0TmFtZT1zcF9hdGY&psc=1', 'https://m.media-amazon.com/images/I/61PrCgQ2KwL._AC_SL1500_.jpg', 1, 0);
INSERT INTO tb_item (name, price, link, image, wishlist_id, source_platform) VALUES ('Lenovo Ideapad 3i', 2250.99, 'https://www.amazon.com.br/Notebook-Lenovo-IdeaPad-i5-1135G7-82MD0007BR/dp/B0B35BGG1P/ref=sr_1_1?__mk_pt_BR=%C3%85M%C3%85%C5%BD%C3%95%C3%91&crid=3JA0N3D1D8663&keywords=ideapad+3i&qid=1698702369&sprefix=ideapad+3%2Caps%2C224&sr=8-1&ufe=app_do%3Aamzn1.fos.25548f35-0de7-44b3-b28e-0f56f3f96147', 'https://m.media-amazon.com/images/I/61PrCgQ2KwL._AC_SL1500_.jpg', 1, 0);

INSERT INTO tb_item_history (fluctuation, item_id) VALUES (0.0, 1);

INSERT INTO tb_item_price (date, price, item_history_id) VALUES ('2023-10-30', 100.0, 1);
INSERT INTO tb_item_price (date, price, item_history_id) VALUES ('2023-11-02', 80.0, 1);

INSERT INTO tb_expense_track (monthly_income, anual_income, extra_income, day_of_salary_payment, fluctuation_by_month, user_id) VALUES (3200.0, 38400.0, 0.0, 5, 0.0, 1);

INSERT INTO tb_total_expense_by_month (date, total_expended, remaining_amount, expense_track_id) VALUES ('2023-10-01', 2000.0, 1200.0, 1);
INSERT INTO tb_total_expense_by_month (date, total_expended, remaining_amount, expense_track_id) VALUES ('2023-11-01', 2000.0, 1200.0, 1);
INSERT INTO tb_total_expense_by_month (date, total_expended, remaining_amount, expense_track_id) VALUES ('2023-12-01', 2000.0, 1200.0, 1);
INSERT INTO tb_total_expense_by_month (date, total_expended, remaining_amount, expense_track_id) VALUES ('2024-01-01', 2000.0, 1200.0, 1);

INSERT INTO tb_fixed_expense (title, price, day_of_charge, begin_of_expense, end_of_expense) VALUES ('Netflix', 400.0, 4, '2022-06-24', '2025-06-24');
INSERT INTO tb_fixed_expense (title, price, day_of_charge, begin_of_expense, end_of_expense) VALUES ('Outra coisa', 500.0, 10, '2022-06-24', '2023-10-01');
INSERT INTO tb_fixed_expense (title, price, day_of_charge, begin_of_expense, end_of_expense) VALUES ('Prime Video', 1100.0, 11, '2023-01-04', '2023-12-01');

INSERT INTO tb_fixed_expenses_by_month (tbem_id, fixed_expense_id) VALUES (1, 1);
INSERT INTO tb_fixed_expenses_by_month (tbem_id, fixed_expense_id) VALUES (1, 2);
INSERT INTO tb_fixed_expenses_by_month (tbem_id, fixed_expense_id) VALUES (1, 3);
