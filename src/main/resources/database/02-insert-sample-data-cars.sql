--liquibase formatted sql
--changeset SpringSecurityValidationHomeWork:2


INSERT INTO cars (producer, model, vin_number, year_of_production, registration_number) VALUES
('Toyota', 'Corolla', 'JTDBR32E720049876', 2018, 'WA12345'),
('Volkswagen', 'Golf', 'WVWZZZ1KZ6W612345', 2016, 'KR1ABC'),
('BMW', 'X5', '5UXFE43598L123456', 2020, 'W0BMW'),
('Audi', 'A4', 'WAUZZZ8K9AA123456', 2015, 'PO9K2'),
('Mercedes-Benz', 'C-Class', 'WDDGF8AB9EA123456', 2019, 'K1FAST');