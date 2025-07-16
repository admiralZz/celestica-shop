-- changeset admin:007-1
INSERT INTO categories (name, description)
VALUES ('Игрушки для малышей', 'Игрушки для детей от 0 до 3 лет');

INSERT INTO categories (name, description)
VALUES ('Развивающие игрушки', 'Игрушки для развития моторики, логики и творческих навыков');

INSERT INTO categories (name, description)
VALUES ('Конструкторы', 'Различные виды конструкторов для детей разных возрастов');

INSERT INTO categories (name, description)
VALUES ('Настольные игры', 'Игры для всей семьи');

INSERT INTO products (id, name, description, price, stock_quantity, category_id)
VALUES (1, 'Мягкий кубик', 'Мягкий развивающий кубик с разными текстурами и звуками', 499.99, 50,
        (SELECT id FROM categories WHERE name = 'Игрушки для малышей'));

INSERT INTO products (id, name, description, price, stock_quantity, category_id)
VALUES (2, 'Деревянные пазлы ''Животные''', 'Набор из 5 пазлов с изображениями животных', 799.99, 30,
        (SELECT id FROM categories WHERE name = 'Развивающие игрушки'));

INSERT INTO products (id, name, description, price, stock_quantity, category_id)
VALUES (3, 'Конструктор ''Город''', 'Конструктор для сборки городской инфраструктуры, 200 деталей', 1299.99, 20,
        (SELECT id FROM categories WHERE name = 'Конструкторы'));

INSERT INTO products (id, name, description, price, stock_quantity, category_id)
VALUES (4, 'Монополия Junior', 'Классическая настольная игра Монополия в детской версии', 999.99, 15,
        (SELECT id FROM categories WHERE name = 'Настольные игры'));