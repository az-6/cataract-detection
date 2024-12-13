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
   - Cloud Storage

   Run the following command to enable them:
   ```bash
   gcloud services enable run.googleapis.com artifactregistry.googleapis.com cloudbuild.googleapis.com storage.googleapis.com
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

## Step 3: Setting Up Google Cloud Storage
1. Create a Cloud Storage bucket to store uploaded images:
   ```bash
   gcloud storage buckets create gs://<your-bucket-name> --location=${REGION}
   ```
2. Ensure the bucket name is globally unique and replace `<your-bucket-name>` with your preferred name.
3. Grant public read access to the bucket if needed:
   ```bash
   gcloud storage buckets add-iam-policy-binding gs://<your-bucket-name> \
       --member="allUsers" \
       --role="roles/storage.objectViewer"
   ```

## Step 4: Building the Container Image
1. Build the container image with Cloud Build. Use `cataract-detection` as the image name and `1.0.0` as the version:
   ```bash
   gcloud builds submit --tag ${REGION}-docker.pkg.dev/${PROJECT_ID}/backend/cataract-detection:1.0.0
   ```
2. Wait until the build completes successfully.

## Step 5: Deploying to Cloud Run
1. Deploy the application using the image from Artifact Registry:
   ```bash
   gcloud run deploy cataract-detection \
       --image ${REGION}-docker.pkg.dev/${PROJECT_ID}/backend/cataract-detection:1.0.0 \
       --region ${REGION} \
       --memory 4Gi \
       --cpu 2 \
       --timeout 800 \
       --set-env-vars BUCKET_NAME=<your-bucket-name>
   ```
2. You will be prompted with several questions during deployment. Respond as follows:
   - **Service name**: Press Enter to use the default name `cataract-detection`.
   - **Region**: Choose the same region as defined earlier.
   - **Allow unauthenticated invocations**: Type `y` to allow public access to the service.

3. Wait for the deployment to complete.

## Step 6: Accessing the Service
1. After deployment, note the service URL provided in the output.
2. The URL will look like this:
   ```
   https://[SERVICE_NAME]-[HASH].[REGION].run.app
   ```
3. Use this URL to access the `/predict` endpoint of your Backend API.

## Cloud Storage Integration
1. Ensure the application uploads images to the specified bucket (`BUCKET_NAME`). The application should:
   - Accept uploaded files via a POST request.
   - Store the uploaded file in the Cloud Storage bucket specified by the `BUCKET_NAME` environment variable.

## Useful Links
- [GCP Console](https://console.cloud.google.com/)
- [Cloud Run Documentation](https://cloud.google.com/run/docs)
- [Artifact Registry Documentation](https://cloud.google.com/artifact-registry/docs)
- [Cloud Build Documentation](https://cloud.google.com/build/docs)
- [Cloud Storage Documentation](https://cloud.google.com/storage/docs)
