# OJack Payment Service
```
Rahadian K Putra      05111640000006
Taufiq Tirtajiwangga  05111640000016
Michael J Albertus    05111640000097
```
Proposal: [Link](https://docs.google.com/document/d/1Z2ShZR6lU_8SY8NFg1wXUiJI6F_bhLzSOYUbOmH1NpI/edit?usp=sharing)

---
# API Detail
## Authorization Token
Every request sent to the service MUST include 'Authorization' in the request's header. The format is following:
```
Authorization: Bearer <jwt_token>
```
This service uses JWT as authorization token, below are the secret key and the JWT token format used by this service:
```
Secret Key: EmYTCuhxT3$FpXUVXDj*f0e4LbcpDAk^apes1eAuUtUrC3V%1XPSGD^2KPj*u^L&
Algorithm: HS512

JWT Payload Structure:
{
  "sub": <wallet_number>,
  "name": <wallet_number_owner_name>,
  "rol": <token_auth_role>,
  "atp": <wallet_number_account_type>
}

Claims Description:
- sub   : Wallet Number is phone number of the registered user. For ADMIN token, its value MUST be 'ROOT'.
- name  : Name of the registered user/wallet. For ADMIN token, its value MUST correspond with the service's name, e.g., 'accouting_service'.
- rol   : ADMIN or USER. ADMIN token is used for service-to-service, while USER is for user-to-service.
- atp   : This only valids for USER token, its possible value is 'customer', 'driver', or 'restaurant'.
```
**NOTE**: The authorization above is (currently) only valid for this service. We're waiting for 'Customer' group to design their JWT structure, secret key, and everything else.  

**You can take a look at sample tokens we made to make things easier for us. You can copy it [HERE](sample_token.md)**

---
## API Resources and Operations
### Wallet Resource
This resource handles 'Wallet' entity.
#### Operations
- Create Wallet:
  - Method  : **POST**
  - Role    : **ADMIN**
  - URL     : **/api/v1/wallet**
  - Sample Request:
    ```
    Request Body:
    {
	    "wallet_number": "6288804862379",
	    "type": "customer"
    }
    
    Description:
    - wallet_number : Phone number of registered user
    - type          : Type of wallet => ['customer', 'driver', 'restaurant']
    ```
  - Sample Response:
    ```
    {
      "id": "5cd6dc041a40fc6cb0936243",
      "balance": 0,
      "type": "customer",
      "created_date": "2019-05-11T14:28:20.069+0000",
      "last_modified_date": "2019-05-11T14:28:20.069+0000",
      "wallet_number": "6288804862379"
    }
    ```
  ---
- List Wallet
  - Method  : **GET**
  - Role    : **ADMIN**,**USER**
  - URL     : **/api/v1/wallet**
  - Sample Request: None
  - Sample Response:
    ```
    [
      {
        "id": "5cd6d3611a40fc6cb0936242",
        "balance": 0,
        "type": "restaurant",
        "created_date": "2019-05-11T13:51:29.048+0000",
        "last_modified_date": "2019-05-11T13:51:29.048+0000",
        "wallet_number": "6288804862378"
      },
      {
        "id": "5cd6dc041a40fc6cb0936243",
        "balance": 0,
        "type": "customer",
        "created_date": "2019-05-11T14:28:20.069+0000",
        "last_modified_date": "2019-05-11T14:28:20.069+0000",
        "wallet_number": "6288804862379"
      }
    ]
    ```
  - Note: **For USER token, the response would be single wallet that correponds to wallet number of the token.**
  ---
- Show Individual Wallet Detail
  - Method  : **GET**
  - Role    : **ADMIN**,**USER**
  - URL     : **/api/v1/wallet/{walletNumber}**
  - Sample Request:
    ```
    Request URL: /api/v1/wallet/6288804862378
    Body: None
    ```
  - Sample Response:
    ```
    {
      "id": "5cd6d3611a40fc6cb0936242",
      "balance": 0,
      "type": "restaurant",
      "created_date": "2019-05-11T13:51:29.048+0000",
      "last_modified_date": "2019-05-11T13:51:29.048+0000",
      "wallet_number": "6288804862378"
    }
    ```
  - Note: **ADMIN can view all details of registered wallets. USER can only see their own wallet detail**
---
### Topup Resource
This resource handles 'Topup' entity. 'Topup' inherits 'Transaction' entity.
- Request for Topup
  - Method  : **POST**
  - Role    : **USER** => **['customer']**
  - URL     : **/api/v1/transaction/topup**
  - Sample Request:
    ```
    Body:
    {
  	  "topup_balance": 812376
    }
  
    Description:
    - topup_balance: amount of balance to add to user's wallet
    ```
  - Sample Response:
    ```
    {
      "id": "5cd6e6c71a40fc6cb0936244",
      "cashflow": "credit",
      "status": "pending",
      "created_date": "2019-05-11T15:14:15.290+0000",
      "last_modified_date": "2019-05-11T15:14:15.290+0000",
      "transaction_type": "TOPUP",
      "transaction_id": "OJAP-TOP-9dc3863c189f5af50cc5be6bef9a75acf66b582a",
      "wallet_number": "6288804862376",
      "topup_balance": 812376
    }
    ```
  ---
- List Topup Transaction
  - Method  : **GET**
  - Role    : **ADMIN**, **USER** => **['customer']**
  - URL     : **/api/v1/transaction/topup**
  - Sample Request: None
  - Sample Response:
    ```
    [
      {
        "id": "5cd6e6c71a40fc6cb0936244",
        "cashflow": "credit",
        "status": "pending",
        "created_date": "2019-05-11T15:14:15.290+0000",
        "last_modified_date": "2019-05-11T15:14:15.290+0000",
        "transaction_type": "TOPUP",
        "transaction_id": "OJAP-TOP-9dc3863c189f5af50cc5be6bef9a75acf66b582a",
        "wallet_number": "6288804862376",
        "topup_balance": 812376
      },
      {
        "id": "5cd6ea491a40fc6cb0936245",
        "cashflow": "credit",
        "status": "pending",
        "created_date": "2019-05-11T15:29:13.063+0000",
        "last_modified_date": "2019-05-11T15:29:13.063+0000",
        "transaction_type": "TOPUP",
        "transaction_id": "OJAP-TOP-15b82eb6ee228e4b2178dca6f19fd783dc60d0bd",
        "wallet_number": "6288804862376",
        "topup_balance": 50000
      }
    ]
    ```
  - Note: **ADMIN can view all topup transaction. USER can only view their wallet's topup transaction**
  ---
- Confirm Topup Transaction
  - Method  : **PATCH**
  - Role    : **ADMIN**
  - URL     : **/api/v1/transaction/topup/confirm/{topupId}**
  - Sample Request:
    ```
    Request URL: /api/v1/transaction/topup/confirm/OJAP-TOP-9dc3863c189f5af50cc5be6bef9a75acf66b582a
    Body: None
    ```
  - Sample Response:
    ```
    {
      "id": "5cd6e6c71a40fc6cb0936244",
      "cashflow": "credit",
      "status": "confirmed",
      "created_date": "2019-05-11T15:14:15.290+0000",
      "last_modified_date": "2019-05-11T15:39:41.271+0000",
      "transaction_type": "TOPUP",
      "transaction_id": "OJAP-TOP-9dc3863c189f5af50cc5be6bef9a75acf66b582a",
      "wallet_number": "6288804862376",
      "topup_balance": 812376
    }
    ```
  ---
- Cancel Topup Transaction
  - Method  : **PATCH**
  - Role    : **ADMIN**
  - URL     : **/api/v1/transaction/topup/cancel/{topupId}**
  - Sample Request:
    ```
    Request URL: /api/v1/transaction/topup/cancel/OJAP-TOP-15b82eb6ee228e4b2178dca6f19fd783dc60d0bd
    Body: None
    ```
  - Sample Response:
    ```
    {
      "id": "5cd6ea491a40fc6cb0936245",
      "cashflow": "credit",
      "status": "canceled",
      "created_date": "2019-05-11T15:29:13.063+0000",
      "last_modified_date": "2019-05-11T15:48:00.072+0000",
      "transaction_type": "TOPUP",
      "transaction_id": "OJAP-TOP-15b82eb6ee228e4b2178dca6f19fd783dc60d0bd",
      "wallet_number": "6288804862376",
      "topup_balance": 50000
    }
    ```
