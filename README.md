# Orders management system

## Task

Create a rest controller for creating user storage.

User:

> Id  
> Name

Create a rest controller for creating orders for the specific user in storage.  
Order fields:

> Id  
> Trade date  
> Amount  
> Status (deleted, valid)

One user can have multiple orders.  
Add method to change orders status -> delete  
Create methods that will return next orders:

- All valid user orders sorted by trade date
- All users with orders sorted by trade date
- All users without orders sorted by trade date
- All users with orders with the specific status
- Sum of all user’s orders
- User’s sorted by sum of amounts of all orders

## Part 2

- Add Product entity
- Add Category entity

One category can have many products and one product can have many categories.  
Order must have at least one product.  
Order can have multiple amount of the same product

#### To Do:

- Update Order creating
- Add CRUD for Products
- Add CRUD for Categories
- Add Queries:
    - Fetch users with the orders that have products from the defined category
    - Fetch categories sorted by order amount

## Part 3

1. Add amount of products that can be bought. If **n** products are in the user order it means the amount of available product **equals = product.availableAmount - n**. 

- Add queries to fetch available products
- Add queries to fetch not available products

2. User can book a product for some period of time. So the amount of booked product items are not available for other users. 
If user doesn't buy these items in n minutes then product items become available again. 

- Add additional field to handle the item status
- Add queries to fetch available products (they are not booked)
- Add cron job to unbook product items

3. Create integration test to simulate situation where multiple users buy the same product items to validate cases if the total amount of bought product items will be not bigger then available amount.



## Requirements

JDBC Template  
PostgreSQL 11  
Spring Data JPA  
Servlet Filter  
Tests to cover the main logic

## JSON examples for REST:

**Create product:**

    {    
      "name":"Anti-Aging Cream with Bird's Nest Extract",
      "categories":[
        {
          "id": 4,
          "name": "Organic cosmetics"
        },{
          "id": 3,
          "name": "Face"
        }
      ]
    }

**Create order:**

    {  
        "tradeDate": "2022-02-02",  
        "amount": 19,  
        "status": "VALID",    
        "products":[  
            {  
              "id":1,  
              "name": "Nourishing Collagen Cream",
              "amount": 12  
            },{
              "id":3,
              "name": "Green Tea Shampoo",
              "amount": 7
            }
        ]    
    }
