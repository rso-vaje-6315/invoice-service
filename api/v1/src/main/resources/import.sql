INSERT INTO invoice_config(id, timestamp, code, config_value) VALUES ('95bc3a18-2ae2-4297-960d-b1b0347093f9', NOW(), 'vat.rate', '0.22');
INSERT INTO invoice_config(id, timestamp, code, config_value) VALUES ('d452d416-93d4-4a41-85aa-86649b8d0c6f', NOW(), 'seller.name', 'Sellers d.o.o');
INSERT INTO invoice_config(id, timestamp, code, config_value) VALUES ('bd62f59a-446a-4291-9ac6-0365a4b9f0fe', NOW(), 'seller.street', 'Majhna ulica');
INSERT INTO invoice_config(id, timestamp, code, config_value) VALUES ('fb57c9e0-0d1a-4d8c-9ece-959ce1b86eb3', NOW(), 'seller.street.num', '35');
INSERT INTO invoice_config(id, timestamp, code, config_value) VALUES ('a2d0c27a-b088-40d2-91c4-89f4bbcb334e', NOW(), 'seller.post', 'Ljubljana');
INSERT INTO invoice_config(id, timestamp, code, config_value) VALUES ('ba30013e-2bb5-4358-ae6c-d32a53d8ec87', NOW(), 'seller.post.code', '1000');
INSERT INTO invoice_config(id, timestamp, code, config_value) VALUES ('c8b63097-92f8-4402-99b9-98b5e3a0362e', NOW(), 'seller.country', 'Slovenia');
INSERT INTO invoice_config(id, timestamp, code, config_value) VALUES ('1183fd6b-eff9-4ca8-80e9-7726528088b3', NOW(), 'seller.phone', '+38613487098');
INSERT INTO invoice_config(id, timestamp, code, config_value) VALUES ('9b7bea35-ad06-415e-ae23-b44e1c707fb2', NOW(), 'seller.tax.num', '6024562335');
INSERT INTO invoice_config(id, timestamp, code, config_value) VALUES ('83436113-0bf2-4534-80c0-49d3aee95cce', NOW(), 'seller.bic', 'BSLJ123784');
INSERT INTO invoice_config(id, timestamp, code, config_value) VALUES ('ee6035bc-3b1e-4f32-8044-c304bea7277d', NOW(), 'seller.iban', 'SI56 2345 1234 9678 990');
INSERT INTO invoice_config(id, timestamp, code, config_value) VALUES ('3ea12e75-c89e-4493-a7f4-0500a7a539c3', NOW(), 'seller.email', 'sales@sellers.com');

INSERT INTO invoices(id, timestamp, customer_id, invoice_url) VALUES ('721430e6-00c4-408a-9215-c0814ab2b540', NOW(), 'customerId', 'https://storage.cloud.google.com/rso-vaje-bucket/1575394160630-invoice-order-invoiceId-customerId.pdf?hl=sl');
