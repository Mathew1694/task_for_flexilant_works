--1.Write a SQL query to find products whose price changed from yesterday to today.
Ans: Select t.product_id, t.product_name, y.price as old_price, t.price as new_price
from products_today t
left join products_yesterday y
on t.product_id = y.product_id
where t.price <> y.price;

--2.Write a SQL query to find products that are new today, meaning they exist in products_today but not in products_yesterday.
Ans: Select t.product_id, t.product_name, t.price, t.status
from products_today t
left join products_yesterday y
on t.product_id = y.product_id
where y.product_id is null;

--3.Write a SQL query to find products that existed yesterday but are missing today.
Ans: Select y.product_id, y.product_name, y.status, y.price
from products_yesterday y
left join products_today t
on t.product_id = y.product_id
where t.product_id is null;

--4.Write a SQL query to find products whose status changed from yesterday to today.
Ans: Select y.product_id , y.product_name , y.status as old_status, t.status as new_status
from products_yesterday y
left join products_today t
on t.product_id = y.product_id
where y.status <> t.status;

--5.Explanation:
--1. Why did you use INNER JOIN, LEFT JOIN, NOT EXISTS, or another approach?
To combine both the tables based on the primary key and extract the common or distinct values.
--2. What would change if product_id was not unique?
The primary key is assigned to every table as unique identifier.
--3. What issue could happen if price or status can be NULL?
<> doesn't detect changes involving NULL because comparisons with NULL return UNKNOWN. To correctly identify changes, I'd use COALESCE()