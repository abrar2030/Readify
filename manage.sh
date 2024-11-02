#!/usr/bin/env bash

# manage.sh
# A unified script for building, deploying, and managing the Readify project.

# Exit immediately if a command exits with a non-zero status,
# treat unset variables as an error, and prevent errors in pipelines.
set -euo pipefail
IFS=$'\n\t'

# Trap errors and provide a message before exiting
trap 'echo "An error occurred. Exiting..."; exit 1;' ERR

echo "----------------------------------------"
echo "Starting build and deployment process..."
echo "----------------------------------------"

# Function to check if a command exists
command_exists() {
  command -v "$1" >/dev/null 2>&1
}

# Function to install missing tools via Homebrew
install_if_missing() {
  if ! command_exists "$1"; then
    echo "Installing $1 via Homebrew..."
    brew install "$1" || { echo "Failed to install $1. Please install it manually."; exit 1; }
  else
    echo "$1 is already installed."
  fi
}

# Check for Homebrew and install if missing
if ! command_exists brew; then
  echo "Homebrew not found. Installing Homebrew..."
  /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

  # Dynamically determine Homebrew installation path
  if [[ -d "/home/linuxbrew/.linuxbrew" ]]; then
    eval "$(/home/linuxbrew/.linuxbrew/bin/brew shellenv)"
  elif [[ -d "/home/$USER/.linuxbrew" ]]; then
    eval "$(/home/$USER/.linuxbrew/bin/brew shellenv)"
  else
    echo "Homebrew installation path not found. Please ensure Homebrew is installed correctly."
    exit 1
  fi
else
  echo "Homebrew is already installed."
fi

# Install required tools if they are missing (excluding docker to prevent conflicts)
tools=(yq dos2unix yamllint eslint mvn npm)
for tool in "${tools[@]}"; do
  install_if_missing "$tool"
done

# Handle Docker Compose separately
COMPOSE_CMD=""
if command_exists "docker" && docker compose version >/dev/null 2>&1; then
  COMPOSE_CMD="docker compose"
elif command_exists "docker-compose"; then
  COMPOSE_CMD="docker-compose"
else
  echo "Docker Compose is not installed. Attempting to install via Homebrew..."
  brew install docker-compose || { echo "Failed to install docker-compose. Please install it manually."; exit 1; }
  COMPOSE_CMD="docker-compose"
fi

# Ensure COMPOSE_CMD is set
if [ -z "$COMPOSE_CMD" ]; then
  echo "Docker Compose command not found. Exiting."
  exit 1
fi

echo "Using Docker Compose command: $COMPOSE_CMD"

# Verify yq installation
echo "Verifying yq installation..."
yq_version=$(yq --version 2>/dev/null)
if [[ $yq_version == yq* ]]; then
  echo "yq is installed: $yq_version"
else
  echo "yq is not properly installed. Please reinstall yq."
  exit 1
fi

# Function to build the Maven project
build_backend() {
    echo "=============================="
    echo "Building Maven Project..."
    echo "=============================="

    mvn clean package -DskipTests
    echo "Maven project built successfully."
}

# Function to build frontend (if applicable)
build_frontend() {
    FRONTEND_DIR="src/main/resources/static/js"
    if [ -f "$FRONTEND_DIR/package.json" ]; then
        echo "================="
        echo "Building Frontend..."
        echo "================="

        echo "----------------------------------"
        echo "Installing npm dependencies..."
        echo "----------------------------------"
        pushd "$FRONTEND_DIR" >/dev/null

        npm install

        echo "----------------------------------"
        echo "Running frontend build..."
        echo "----------------------------------"
        npm run build
        echo "Frontend built successfully."
        popd >/dev/null
        echo "----------------------------------------"
    else
        echo "No frontend package.json found in $FRONTEND_DIR. Skipping frontend build."
        echo "----------------------------------------"
    fi
}

# Function to build Docker images
build_docker_images() {
    echo "====================="
    echo "Building Docker Images..."
    echo "====================="

    # Ensure Dockerfile exists
    if [ ! -f "Dockerfile" ]; then
        echo "Error: Dockerfile not found in the project root."
        exit 1
    fi

    echo "----------------------------------"
    echo "Building Docker image for Readify..."
    echo "----------------------------------"

    docker build -t readify:latest .
    echo "Docker image for Readify built successfully."
    echo "----------------------------------------"
}

# Function to deploy or start services using Docker Compose
deploy_or_start_services() {
    ACTION=$1
    echo "=============================="
    echo "Performing Docker Compose: $ACTION..."
    echo "=============================="

    if [ -f "docker-compose.yaml" ] || [ -f "docker-compose.yml" ]; then
        echo "----------------------------------"
        echo "Executing Docker Compose $ACTION..."
        echo "----------------------------------"
        if [ "$ACTION" == "deploy" ]; then
            $COMPOSE_CMD up --build -d
        elif [ "$ACTION" == "start" ]; then
            $COMPOSE_CMD up -d
        else
            echo "Invalid action for Docker Compose."
            exit 1
        fi
        echo "Docker Compose action '$ACTION' completed successfully."
        echo "----------------------------------------"
    else
        echo "Error: docker-compose.yaml or docker-compose.yml not found in the root directory."
        exit 1
    fi
}

# Function to stop services using Docker Compose
stop_services() {
    echo "=============================="
    echo "Stopping Services with Docker Compose..."
    echo "=============================="

    if [ -f "docker-compose.yaml" ] || [ -f "docker-compose.yml" ]; then
        echo "----------------------------------"
        echo "Stopping Docker Compose..."
        echo "----------------------------------"
        $COMPOSE_CMD down
        echo "All services stopped successfully."
        echo "----------------------------------------"
    else
        echo "Error: docker-compose.yaml or docker-compose.yml not found in the root directory."
        exit 1
    fi
}

# Function to display usage
usage() {
    echo "Usage: $0 {build|start|stop|deploy|all}"
    echo "  build  - Builds the backend, frontend, and Docker images."
    echo "  start  - Starts all services using Docker Compose."
    echo "  stop   - Stops all services using Docker Compose."
    echo "  deploy - Builds and deploys all services."
    echo "  all    - Performs build, deploy, and starts all services."
    exit 1
}

# Function to check if Docker is running
check_docker_running() {
    if ! docker info >/dev/null 2>&1; then
        echo "Docker is not running. Please start Docker Desktop and ensure WSL integration is enabled."
        exit 1
    fi
}

# Main script execution
main() {
    # Ensure script is run from the project root by checking for pom.xml
    if [ ! -f "pom.xml" ]; then
        echo "Error: pom.xml not found. Please run this script from the project root."
        exit 1
    fi

    # Check if Docker is running
    check_docker_running

    # Parse command-line arguments
    case "$1" in
        build)
            build_backend
            build_frontend
            build_docker_images
            ;;
        start)
            deploy_or_start_services "start"
            ;;
        stop)
            stop_services
            ;;
        deploy)
            build_backend
            build_frontend
            build_docker_images
            deploy_or_start_services "deploy"
            ;;
        all)
            build_backend
            build_frontend
            build_docker_images
            deploy_or_start_services "deploy"
            ;;
        *)
            usage
            ;;
    esac
}

# Invoke main with all script arguments
main "$@"
