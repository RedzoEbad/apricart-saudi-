#!/bin/bash
set -e

# ─── CONFIGURATION ───────────────────────────────────────────────
SERVER_IP="185.164.25.196"
SERVER_USER="ubuntu"
SSH_KEY="/Users/mac/Downloads/2666_private.rsa"
APP_DIR="/home/ubuntu/apricart-saudi-"
IMAGE_NAME="apricart-api:latest"
TAR_NAME="apricart-api.tar"

echo ""
echo "╔══════════════════════════════════════════════╗"
echo "║     Apricart API — Docker Deployment         ║"
echo "╚══════════════════════════════════════════════╝"
echo ""

# ─── STEP 1: Build Docker image locally ──────────────────────────
echo "▶ Step 1/5: Building Docker image locally..."
docker build -t $IMAGE_NAME .
echo "✅ Image built successfully."
echo ""

# ─── STEP 2: Save and compress image ─────────────────────────────
echo "▶ Step 2/5: Saving and compressing Docker image..."
docker save $IMAGE_NAME | gzip > /tmp/$TAR_NAME.gz
SIZE=$(du -sh /tmp/$TAR_NAME.gz | cut -f1)
echo "✅ Image saved ($SIZE compressed)."
echo ""

# ─── STEP 3: Transfer to server ──────────────────────────────────
echo "▶ Step 3/5: Transferring image to server ($SERVER_IP)..."
scp -i $SSH_KEY -o StrictHostKeyChecking=no \
    /tmp/$TAR_NAME.gz \
    docker-compose.yml \
    .env \
    $SERVER_USER@$SERVER_IP:/tmp/
echo "✅ Files transferred."
echo ""

# ─── STEP 4: Transfer google/firebase config ─────────────────────
echo "▶ Step 4/5: Transferring Firebase config..."
ssh -i $SSH_KEY -o StrictHostKeyChecking=no $SERVER_USER@$SERVER_IP \
    "mkdir -p $APP_DIR/google $APP_DIR/logs"
scp -i $SSH_KEY -r google/ $SERVER_USER@$SERVER_IP:$APP_DIR/
echo "✅ Firebase config transferred."
echo ""

# ─── STEP 5: Deploy on server ────────────────────────────────────
echo "▶ Step 5/5: Deploying on server..."
ssh -i $SSH_KEY -o StrictHostKeyChecking=no $SERVER_USER@$SERVER_IP << 'REMOTE'
set -e

APP_DIR="/home/ubuntu/apricart-saudi-"
TAR_NAME="apricart-api.tar"
IMAGE_NAME="apricart-api:latest"

echo "  → Loading Docker image (this may take a minute)..."
gunzip -c /tmp/$TAR_NAME.gz | docker load

echo "  → Copying deployment files..."
cp /tmp/docker-compose.yml $APP_DIR/
cp /tmp/.env $APP_DIR/
cd $APP_DIR

echo "  → Stopping existing container (if any)..."
docker compose down --remove-orphans 2>/dev/null || true

echo "  → Starting new container..."
docker compose up -d

echo "  → Waiting for health check (60s)..."
sleep 20

STATUS=$(docker inspect --format='{{.State.Health.Status}}' apricart_api 2>/dev/null || echo "unknown")
echo "  → Container health: $STATUS"

docker compose logs --tail=20

echo "  → Cleanup..."
rm -f /tmp/$TAR_NAME.gz /tmp/docker-compose.yml /tmp/.env
REMOTE

echo ""
echo "╔══════════════════════════════════════════════╗"
echo "║  ✅ Deployment Complete!                     ║"
echo "║  🌐 API: http://185.164.25.196:8081          ║"
echo "╚══════════════════════════════════════════════╝"
echo ""
