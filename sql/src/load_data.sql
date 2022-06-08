COPY MENU
FROM '/Users/jairedjawed/Downloads/project/data/menu.csv'
WITH DELIMITER ';';

COPY USERS
FROM '/Users/jairedjawed/Downloads/project/data/users.csv'
WITH DELIMITER ';';

COPY ORDERS
FROM '/Users/jairedjawed/Downloads/project/data/orders.csv'
WITH DELIMITER ';';
ALTER SEQUENCE orders_orderid_seq RESTART 87257;

COPY ITEMSTATUS
FROM '/Users/jairedjawed/Downloads/project/data/itemStatus.csv'
WITH DELIMITER ';';

