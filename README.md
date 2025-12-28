
# 🗺️ Map2D Project  
**Visualize, Edit, and Explore 2D Grids with Colors, Obstacles, and Paths**

---

## 📌 Overview  
This project provides a simple framework for working with **2D grid maps** in Java. It combines **graphical visualization**, **pathfinding**, and **map editing** tools to create interactive demos and educational projects.

---

## ✨ Features  
- ✅ **Grid Visualization** – Render maps using a graphical interface with customizable colors and scaling.  
- ✅ **Color-Coded Cells** – Represent empty spaces, obstacles, and special zones using color codes or RGB values.  
- ✅ **Obstacles & Shapes** – Add walls, circles, and other shapes to create complex environments.  
- ✅ **Pathfinding** – Compute and display shortest paths between two points on the grid.  
- ✅ **Distance Mapping** – Generate a heatmap of distances from a starting point to all reachable cells.  
- ✅ **Flood Fill** – Fill connected regions with a new color, similar to paint bucket tools.  
- ✅ **File I/O** – Save maps to text files and load them back for visualization or processing.  

---

## 🖌️ Drawing Maps  
- Each cell is drawn as a square on a canvas.  
- Supports **color gradients**, **palette-based colors**, or **custom RGB values**.  
- Optional grid lines for clarity.  
- Handles **cyclic wrapping** for toroidal maps.  

---

## 🎨 Colors  
Two approaches for color representation:  
- **Palette Codes**: Simple integers mapped to predefined colors.  
- **RGB/ARGB Values**: Full flexibility for custom colors and transparency.  

---

## 🧭 Pathfinding  
- Uses **Breadth-First Search (BFS)** for shortest paths in grids.  
- Supports **cyclic wrapping** for edge-to-edge connectivity.  
- Optional diagonal movement (configurable).  
- Visualize paths as **dots** or **continuous lines**.  

---

## 🔍 Distance Maps  
- Compute distances from a starting point to all reachable cells.  
- Visualize as a gradient heatmap for intuitive understanding.  

---

## 🪣 Flood Fill  
- Fill connected regions with a new color.  
- Works with cyclic wrapping for edge-connected areas.  

---

## 📂 Save & Load  
Maps stored in a simple
Easy to share and reload for experiments.

---

## 🧩 Shapes  
- Draw circles, lines, and patterns directly on the grid.  
- Combine shapes to create obstacles or decorative elements.  

---

## ✅ Use Cases  
- Pathfinding demonstrations  
- Game map editors  
- Algorithm visualization (BFS, flood fill)  
- Educational projects for data structures and AI  

---

## 🚀 Getting Started  

- Create a map with desired dimensions.
- Add obstacles or shapes using color codes.
- Visualize the map with the GUI.
- Compute paths or distance maps and overlay them.
- Save your work and reload anytime.

---

## 💡 Tips

- Flip Y-axis if your grid origin is top-left (StdDraw uses bottom-left- Flip Y-axis if your grid origin is top-left (StdDraw uses bottom-left).
- Enable double-buffering for smooth rendering

![Get-Photo](https://github.com/OfeksoN/I2CS_Ex2/blob/3a57775f7639bdf629af7f02fdeac81faaebb204/ShortestPath-Pikachu.jpg)
