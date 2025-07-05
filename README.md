# 🌳 CART Feature Importance Evaluator

This project aims to determine how important each input attribute in a dataset is for the accuracy of a classification model, using the **CART (Classification and Regression Trees)** algorithm.

It systematically tests the impact of each attribute by training models with and without each feature and evaluating the change in prediction performance.

---

## 🧠 Motivation

In real-world datasets, not all features contribute equally to the quality of prediction. This tool helps visualize and quantify the **sensitivity of a model to individual attributes**, aiding in **feature selection and model interpretation**.

---

## 🚀 Features

- ✅ Fully custom implementation of the **CART algorithm** in Java  
- 🔄 Performs **100 trainings and predictions** for each test case (with and without a specific attribute)  
- 📊 Calculates **average F1 score** to measure performance  
- 📈 Ranks attributes based on their **impact on prediction quality**  
- 📋 Displays detailed metrics:
  - **Precision**, **Recall**, and **F1 Score** for each class
  - **Macro-averaged metrics** across all classes  
- 🌳 Visualizes one example **decision tree** for each case (attribute omitted)  
- 🧪 Comes with **sample datasets** for easy testing  

---

## 🛠 Tech Stack

| Component        | Technology            |
|------------------|------------------------|
| Core logic       | Java (custom CART)     |
| Build tool       | Maven                  |
| Visualization    | Node.js + Express      |
| Frontend         | HTML, CSS              |

---

## 🧪 How It Works

1. Load a dataset (CSV/TXT/XLSX format)  
2. Train the CART model with **all features** – store average metrics  
3. For each attribute:
   - Exclude it from the dataset  
   - Train 100 models and perform 100 predictions  
   - Calculate average metrics  
   - Visualize a resulting decision tree  
4. Rank all attributes by drop in performance when excluded  

---

## 🧪 Sample Datasets

You can find example datasets in the `/datasets` folder. These are pre-formatted for direct use with the tool.

---

## 🎓 Ideal For

- Machine Learning students and researchers  
- Developers working on **feature selection**  
- Anyone exploring **model explainability** in decision trees  

---

## 👨‍💻 About Me

This project was built as part of an academic research initiative and personal interest in machine learning explainability.  
The **entire CART algorithm and feature ranking logic** were implemented from scratch in Java.

---

## 📄 License

This is an open educational project. For contributions, feedback, or collaboration opportunities, feel free to reach out:  
📧 **jakubgaly77@gmail.com**
