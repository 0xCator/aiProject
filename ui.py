from PyQt5 import QtWidgets, uic
import sys

class Ui(QtWidgets.QMainWindow):
    def __init__(self):
        super(Ui, self).__init__()
        uic.loadUi('codesmell.ui', self)

        self.setFixedSize(802, 629)

        self.resultTable.horizontalHeader().setSectionResizeMode(QtWidgets.QHeaderView.Stretch)
        self.resultTable.verticalHeader().setSectionResizeMode(QtWidgets.QHeaderView.Stretch)

        self.importFile.clicked.connect(self.getFile)

        self.ResultPage = self.findChild(QtWidgets.QWidget, 'resultsPage')
        self.tabWidget.removeTab(2)

        self.show()

    def getFile(self):
        file = QtWidgets.QFileDialog.getOpenFileName(self, 'Open File', '', 'Python Files (*.py)')
        print(file)

        if file[0]:
            self.tabWidget.addTab(self.ResultPage, 'Results')
            for i in range(5):
                self.resultTable.setItem(i, 1, QtWidgets.QTableWidgetItem(f'Test{i}'))
            self.tabWidget.setCurrentIndex(2)

app = QtWidgets.QApplication(sys.argv)
window = Ui()
app.exec_()