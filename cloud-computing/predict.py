import tensorflow as tf
import numpy as np

def predict_catarac(image_paths, model, threshold=0.5):
    results = []
    for image_path in image_paths:
        img = tf.keras.utils.load_img(image_path, target_size=(120, 120))
        img_array = tf.keras.utils.img_to_array(img)
        img_array = np.expand_dims(img_array, axis=0)
        
        prediction = model.predict(img_array)[0][0]
        prediction = np.clip(prediction, 0, 1)

        if prediction < threshold:
            condition = 'Cataract'
            prediction_percentage = round((1 - prediction) * 100, 2)  
        else:
            condition = 'Normal'
            prediction_percentage = round(prediction * 100, 2)  

        print(f"Prediction score: {prediction_percentage:.2f}%")
        print(f"The eye condition is: {condition}")

        result_entry = {
            "condition": condition,
            "prediction_score": f"{prediction_percentage}%"
        }
        results.append(result_entry)
    return results

model = tf.keras.models.load_model('model/cataract_model.h5')
