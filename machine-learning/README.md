#Using CNN Model Guide: Connecting to Our Datasets and Retraining The Model if Needed.

1. Connecting to Our Datasets.
    First, make a shortcut from our google drive dataset ([Dataset_Capstone_Drive](https://www.google.com/url?q=https%3A%2F%2Fdrive.google.com%2Fdrive%2Ffolders%2F1pvYAlszXocWbbZiW7kigFqJQwHEmxcrz%3Fhl%3DID)) to use our data for training and testing.
    Here are the steps:

    * Click on "datasetCapstone" folder.
    * Click "Organize"
    * Click "Add shortcut"
    * Choose "My Drive"
    * Click "Add"

2. Version
    You need to change the version of Keras and Tensorflow to 2.15.0 in order for the ```python model.save("model.h5", save_format="h5")``` code to be executed, so it can save the model in h5 format. And you should also install tabulate as shown on the code below:

    ```python
    !pip uninstall tensorflow
    !pip install tensorflow==2.15.0

    !pip uninstall keras
    !pip install keras==2.15.0

    !pip install tabulate
    ```

    For other versions, please kindly check on the requirements.txt file.

3. Data Visualization.
    You could just start the codes cells on the ipynb file, and it will visualize the data.

4. Augmentation.
    You could change the augmentation procedure on the ```python def create_augmentation_model()``` Function.

5. Model
    You could change the augmentation procedure on the ```python def create_model()``` Function.

6. Retraining The Model.
    You could change the numbers of epochs on 
    ```python
    history = model.fit(
        training_dataset,
        epochs=100,
        validation_data=validation_dataset,
    )
    ```
7. Saving The Model
    After training the Model, you could save the data in h5 format with ```python model.save("model.h5", save_format="h5")``` code.

