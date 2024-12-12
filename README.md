# Deployment Guide: Deploying Backend API with Google Cloud Run

This guide provides step-by-step instructions for deploying your Backend API on Google Cloud Run.

## Prerequisites
Before starting, ensure the following:

1. **Google Cloud Project**
   - You have a GCP project with an active billing account linked to it.

2. **Required APIs Enabled**
   Enable the following APIs for your project:
   - Cloud Run
   - Artifact Registry
   - Cloud Build
   
   Run the following command to enable them:
   ```bash
   gcloud services enable run.googleapis.com artifactregistry.googleapis.com cloudbuild.googleapis.com
   ```

3. **Google Cloud CLI** (optional but recommended)
   - Install the [Google Cloud CLI](https://cloud.google.com/sdk/docs/install).

4. **Authenticate and Configure Project**
   Authenticate and set up your project in the terminal:
   ```bash
   gcloud auth login
   gcloud config set project <your_project_ID>
   ```

## Step 1: Cloning the Repository
1. Clone your repository:
   ```bash
   git clone <repository-url>
   ```
2. Navigate to the project directory:
   ```bash
   cd <project-directory>
   ```

## Step 2: Setting Up Artifact Registry
1. Define environment variables for your project and region:
   ```bash
   export PROJECT_ID=<your_project_ID>
   export REGION=<your_region>
   ```

2. Create an Artifact Registry repository:
   ```bash
   gcloud artifacts repositories create backend \
       --repository-format=docker \
       --location=${REGION} \
       --async
   ```

## Step 3: Building the Container Image
1. Build the container image with Cloud Build. Use `cataract-detection` as the image name and `1.0.0` as the version:
   ```bash
   gcloud builds submit --tag ${REGION}-docker.pkg.dev/${PROJECT_ID}/backend/cataract-detection:1.0.0
   ```
2. Wait until the build completes successfully.

## Step 4: Deploying to Cloud Run
1. Deploy the application using the image from Artifact Registry:
   ```bash
   gcloud run deploy cataract-detection \
       --image ${REGION}-docker.pkg.dev/${PROJECT_ID}/backend/cataract-detection:1.0.0 \
       --region ${REGION} \
       --memory 4Gi \
       --cpu 2 \
       --timeout 800
   ```

2. You will be prompted with several questions during deployment. Respond as follows:
   - **Service name**: Press Enter to use the default name `cataract-detection`.
   - **Region**: Choose the same region as defined earlier.
   - **Allow unauthenticated invocations**: Type `y` to allow public access to the service.

3. Wait for the deployment to complete.

## Step 5: Accessing the Service
1. After deployment, note the service URL provided in the output.
2. The URL will look like this:
   ```
   https://[SERVICE_NAME]-[HASH].[REGION].run.app
   ```
3. Use this URL to access the `/predict` endpoint of your Backend API.

## Useful Links
- [GCP Console](https://console.cloud.google.com/)
- [Cloud Run Documentation](https://cloud.google.com/run/docs)
- [Artifact Registry Documentation](https://cloud.google.com/artifact-registry/docs)
- [Cloud Build Documentation](https://cloud.google.com/build/docs)

---

