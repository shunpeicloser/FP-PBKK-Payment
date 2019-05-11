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
**NOTE**: The authorization above is (currently) only valid for this service only. We're waiting for group 'Customer' to design their JWT structure, secret key, and everything else.
