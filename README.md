# FP-PBKK-Payment
Proposal: https://docs.google.com/document/d/1Z2ShZR6lU_8SY8NFg1wXUiJI6F_bhLzSOYUbOmH1NpI/edit?usp=sharing

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
**NOTE**: The authorization above is (currently) only valid for this service. We're waiting for group 'Customer' to design their JWT structure, secret key, and everything else.

---
## API Resources and Operations
### Wallet Resource
This resource handles 'Wallet' entity.
#### Operations
- Create Wallet:
  - Method  : **POST**
  - Role    : **ADMIN**
  - URL     : **/api/v1/wallet**
  - Request Example:
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
  - Response Example:
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
  - Request Example: None
  - Response Example:
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
  - Request Example:
  ```
  Request URL: /api/v1/wallet/6288804862378
  Body: None
  ```
  - Response Example:
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
  - Note: **ADMIN can see all details of registered wallets. USER can only see their own wallet detail**
---
