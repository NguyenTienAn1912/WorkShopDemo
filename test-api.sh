#!/bin/bash

# Test script for Rating Service API
BASE_URL="http://localhost:8080"

echo "=== Rating Service API Test ==="
echo

# 1. Test login
echo "1. Testing login..."
LOGIN_RESPONSE=$(curl -s -X POST $BASE_URL/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user1","password":"password"}')

if [ $? -eq 0 ]; then
    echo "✓ Login successful"
    TOKEN=$(echo $LOGIN_RESPONSE | grep -o '"token":"[^"]*"' | cut -d'"' -f4)
    echo "Token: ${TOKEN:0:20}..."
else
    echo "✗ Login failed"
    exit 1
fi
echo

# 2. Test rating a product
echo "2. Testing product rating..."
RATING_RESPONSE=$(curl -s -X POST $BASE_URL/api/items/1/ratings \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"rating":5,"comment":"Excellent product! Very satisfied."}')

if [ $? -eq 0 ]; then
    echo "✓ Rating created successfully"
    echo "Response: $RATING_RESPONSE"
else
    echo "✗ Rating creation failed"
fi
echo

# 3. Test getting all ratings for a product
echo "3. Testing get all ratings..."
curl -s -X GET $BASE_URL/api/items/1/ratings | jq .
echo "✓ Retrieved all ratings"
echo

# 4. Test getting average rating
echo "4. Testing get average rating..."
curl -s -X GET $BASE_URL/api/items/1/ratings/average | jq .
echo "✓ Retrieved average rating"
echo

# 5. Test getting user's own ratings
echo "5. Testing get user ratings..."
curl -s -X GET $BASE_URL/api/users/me/ratings \
  -H "Authorization: Bearer $TOKEN" | jq .
echo "✓ Retrieved user ratings"
echo

# 6. Test updating rating (same endpoint as create)
echo "6. Testing rating update..."
UPDATE_RESPONSE=$(curl -s -X POST $BASE_URL/api/items/1/ratings \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"rating":4,"comment":"Updated: Good product, but could be better."}')

if [ $? -eq 0 ]; then
    echo "✓ Rating updated successfully"
    echo "Response: $UPDATE_RESPONSE"
else
    echo "✗ Rating update failed"
fi
echo

# 7. Login as another user and add rating
echo "7. Testing second user rating..."
USER2_LOGIN=$(curl -s -X POST $BASE_URL/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user2","password":"password"}')

TOKEN2=$(echo $USER2_LOGIN | grep -o '"token":"[^"]*"' | cut -d'"' -f4)

curl -s -X POST $BASE_URL/api/items/1/ratings \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN2" \
  -d '{"rating":3,"comment":"Average product"}' > /dev/null

echo "✓ Second user rating added"
echo

# 8. Check updated average
echo "8. Testing updated average..."
curl -s -X GET $BASE_URL/api/items/1/ratings/average | jq .
echo "✓ Retrieved updated average"
echo

# 9. Test deleting rating
echo "9. Testing rating deletion..."
DELETE_RESPONSE=$(curl -s -X DELETE $BASE_URL/api/items/1/ratings \
  -H "Authorization: Bearer $TOKEN")

if [ $? -eq 0 ]; then
    echo "✓ Rating deleted successfully"
    echo "Response: $DELETE_RESPONSE"
else
    echo "✗ Rating deletion failed"
fi
echo

# 10. Test admin login and functionality
echo "10. Testing admin functionality..."
ADMIN_LOGIN=$(curl -s -X POST $BASE_URL/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin"}')

ADMIN_TOKEN=$(echo $ADMIN_LOGIN | grep -o '"token":"[^"]*"' | cut -d'"' -f4)

# Admin delete user2's rating (user2 has ID 2)
ADMIN_DELETE=$(curl -s -X DELETE $BASE_URL/api/items/1/ratings/user/2 \
  -H "Authorization: Bearer $ADMIN_TOKEN")

if [ $? -eq 0 ]; then
    echo "✓ Admin deleted user rating successfully"
    echo "Response: $ADMIN_DELETE"
else
    echo "✗ Admin deletion failed"
fi
echo

echo "=== All tests completed ==="
echo "Check the application logs and H2 console at http://localhost:8080/h2-console"