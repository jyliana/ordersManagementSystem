# Orders management system

## Task

Create a rest controller for creating user storage.

User:

    Id
    Name
Create a rest controller for creating orders for the specific user in storage.

Order fields:

    Id
    Trade date
    Amount
    Status (deleted, valid)

One user can have multiple orders.

Add method to change orders status -> delete

Create methods that will return next orders:

    All valid user orders sorted by trade date
    All users with orders sorted by trade date
    All users without orders sorted by trade date
    All users with orders with the specific status
    Sum of all user’s orders
    User’s sorted by sum of amounts of all orders

## Requirements

JDBC Template

PostgreSQL 11

Spring Data JPA

Servlet Filter

Tests to cover the main logic
