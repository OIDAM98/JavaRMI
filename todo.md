Todo list for RMI
===

## Model Classes
* subasta.Oferta
* subasta.Cliente
* subasta.Producto
- [ ] Add comments to each one
- [ ] Getters
- [x] Only those...?

## Server

- [ ] Investigate/Decide embedded databases
    * H2
    * HSQLDB
    * Apache Derby
- [ ] Implement selected database
    * Backup plan: Text file (maybe csv)
- [x] Make server interface (extends Remote)
- [x] Make server servant (implement previous point)
- [ ] Make server propagate changes to ALL clients
    * Connect controller with server => server has list of controllers

## Controller
- [x] Fix methods involved with Model Classes
- [x] Connect with server
- [x] Connect with GUI

## Client (GUI)
- [ ] Modify current GUI to display added information
- [x] Add login view/window

## After
- [ ] Make demo
- [ ] Make report

# Read More
- Small RMI tutorial + explanation => http://vis.usal.es/rodrigo/documentos/sisdis/seminarios/JavaRMI.pdf
- Example: RMI + JavaFX = Chatroom => https://github.com/Oshan96/ChatRoomFX
- Example: RMI + JavaSwing = Chat => https://github.com/Lifka/Chat-Java-RMI/blob/master/RMIChatCliente/src/GUI/Ventana.java
- Java + bids => https://www.javaworld.com/article/2076379/write-high-performance-rmi-servers-and-swing-clients.html
