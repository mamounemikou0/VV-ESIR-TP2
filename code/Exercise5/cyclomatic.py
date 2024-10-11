import pandas as pd
import matplotlib.pyplot as plt

# Lire le fichier CSV
df = pd.read_csv('cyclomatic-complexity-report.csv', sep='|')

 # voir les colonnes 
print(df.columns) 

# Nettoyer les noms de colonnes si nécessaire
df.columns = df.columns.str.strip()

complexity_column = 'Cyclomatic Complexity'  

# Compter la fréquence de chaque niveau de complexité
frequency = df[complexity_column].value_counts().sort_index()

# Générer l'histogramme
plt.figure(figsize=(10, 6))
plt.bar(frequency.index, frequency.values, color='skyblue')
plt.xlabel('Cyclomatic Complexity')
plt.ylabel('Frequency')
plt.title('Frequency of Cyclomatic Complexity')

# Afficher l'histogramme
plt.xticks(frequency.index)  #  Afficher les valeurs de complexité sur l'axe x
plt.tight_layout()
plt.show()
