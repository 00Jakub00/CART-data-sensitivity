🌳 CART Feature Importance Evaluator
This project explores how each input attribute in a dataset influences the performance of a classification model using the CART (Classification and Regression Trees) algorithm.
It evaluates feature importance by comparing model performance with and without each feature, using repeated training and testing cycles.

🔍 Purpose
In real-world data, some features contribute more to prediction quality than others. 
This tool helps identify those key features and quantify their impact, making it useful for feature selection and model interpretation.

🛠️ Key Features
✅ Custom implementation of the CART algorithm in Java (no external libraries)

🔁 Performs 100 training and prediction cycles per feature test

📊 Calculates average F1 score, Precision, and Recall for evaluation

📉 Ranks features by drop in performance when excluded

🌳 Generates a sample decision tree for each case

🧪 Includes sample datasets and supports custom datasets (CSV, TXT, XLSX)

🧪 How It Works
Load the dataset

Train the model with all features and calculate baseline metrics

For each feature:

Remove the feature from the dataset

Train and test the model 100 times

Compute average metrics

Compare results and rank features based on performance impact

Visualize a decision tree for each feature-omission case

💻 Tech Stack
Component	Technology
Core logic	Java (custom CART)
Build tool	Maven
Visualization	Node.js + Express
Frontend	HTML, CSS

📂 Custom Dataset Instructions
To use your own dataset, copy the file into the src/main/java/sety folder.
Keep the file containing "testovaci_set" in its name for technical reasons – it’s ignored during execution but must be present.

🎯 Suitable For
Students and researchers working with machine learning

Developers exploring model explainability

Anyone interested in feature selection using decision trees

▶️ How to Run
Clone the repository:
git clone https://github.com/00Jakub00/CART-data-sensitivity.git

Open the project in an IDE that supports Maven (e.g., IntelliJ IDEA, Eclipse)

Let Maven download dependencies automatically

Run the Main class located in src/main/java as a Java application

👨‍💻 About the Project
This tool was developed as part of an academic project focused on model explainability. The CART algorithm and feature evaluation logic were built entirely from scratch in Java.

📄 License

This is an open educational project. For contributions, feedback, or collaboration opportunities, feel free to reach out:  
📧 **jakubgaly77@gmail.com**
