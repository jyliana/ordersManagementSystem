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
