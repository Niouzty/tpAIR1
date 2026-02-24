#!/bin/bash

# Script de test de l'API REST
# Usage: ./test_api.sh

API_BASE_URL="http://localhost:8080/tpAIR1/api"
TOKEN=""

echo "=========================================="
echo "Tests API REST - TP3 MasterAnnonce"
echo "=========================================="
echo ""

# Couleurs
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Test 1: Hello World
echo -e "${YELLOW}[TEST 1] GET /api/helloWorld${NC}"
curl -s -X GET "$API_BASE_URL/helloWorld" | jq .
echo ""

# Test 2: Hello World avec QueryParams
echo -e "${YELLOW}[TEST 2] GET /api/helloWorld/with-query${NC}"
curl -s -X GET "$API_BASE_URL/helloWorld/with-query?nom=Dupont&prenom=Jean" | jq .
echo ""

# Test 3: Login
echo -e "${YELLOW}[TEST 3] POST /api/auth/login${NC}"
LOGIN_RESPONSE=$(curl -s -X POST "$API_BASE_URL/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}')

echo "$LOGIN_RESPONSE" | jq .

# Extraire le token
TOKEN=$(echo "$LOGIN_RESPONSE" | jq -r '.token')
echo -e "${GREEN}Token obtenu: $TOKEN${NC}"
echo ""

# Test 4: Lister les annonces
echo -e "${YELLOW}[TEST 4] GET /api/annonces (protégé)${NC}"
curl -s -X GET "$API_BASE_URL/annonces?page=0&size=10" \
  -H "Authorization: Bearer $TOKEN" | jq .
echo ""

# Test 5: Créer une annonce
echo -e "${YELLOW}[TEST 5] POST /api/annonces (protégé)${NC}"
curl -s -X POST "$API_BASE_URL/annonces" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "titre": "Test Annonce",
    "description": "Une belle annonce de test",
    "categorie": "Test",
    "prix": 100.00
  }' | jq .
echo ""

# Test 6: Erreur - Accès sans token
echo -e "${YELLOW}[TEST 6] GET /api/annonces SANS token (devrait être 401)${NC}"
curl -s -X GET "$API_BASE_URL/annonces" | jq .
echo ""

# Test 7: Logout
echo -e "${YELLOW}[TEST 7] POST /api/auth/logout${NC}"
curl -s -X POST "$API_BASE_URL/auth/logout" \
  -H "Authorization: Bearer $TOKEN" | jq .
echo ""

echo -e "${GREEN}=========================================="
echo "Tests terminés !"
echo "==========================================${NC}"
