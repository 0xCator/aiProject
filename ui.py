from PyQt5 import QtWidgets, uic, QtCore
from PyQt5.QtCore import QThread
import sys
import modelrunner
import classparser
import numpy as np

START_DIR = 'ModelSaves/'
MODELS = ['codebert', 'distilbert', 'graphcodebert', 'unixcoder', 'codet5']

class Worker(QThread):
    update_progress = QtCore.pyqtSignal(int, int)
    result_ready = QtCore.pyqtSignal(object)

    def __init__(self, models, parser):
        super(Worker, self).__init__()
        self.parser = parser
        self.models = models

    def run(self):
        total_smells = []
        full_methods = self.parser.get_full_methods()
        full_prototypes = self.parser.get_method_prototypes()

        total_progress = (len(full_methods) + len(full_prototypes) + 1 ) * len(self.models)
        current_progress = 0

        for model in self.models:
            model_smells = []

            model_smells.append(self.models[model].run_model(self.parser.code))
            current_progress += 1
            self.update_progress.emit(current_progress, total_progress)

            for method in full_methods:
                model_smells.append(self.models[model].run_model(method))
                current_progress += 1
                self.update_progress.emit(current_progress, total_progress)
            
            for prototype in full_prototypes:
                model_smells.append(self.models[model].run_model(prototype))
                current_progress += 1
                self.update_progress.emit(current_progress, total_progress)

            # concatenate all the smells, run_model returns an NDArray
            model_smells = np.concatenate(model_smells)
            model_smells = np.unique(model_smells)

            model_smells = ', '.join(model_smells) if model_smells.size > 0 else 'No smells detected'

            total_smells.append(model_smells)
        
        self.result_ready.emit(total_smells)
        

class Ui(QtWidgets.QMainWindow):
    def __init__(self):
        super(Ui, self).__init__()
        uic.loadUi('codesmell.ui', self)

        self.models = {}
        for model in MODELS:
            self.models[model] = modelrunner.ModelRunner(START_DIR + model)

        self.setFixedSize(802, 629)

        self.resultTable.horizontalHeader().setSectionResizeMode(QtWidgets.QHeaderView.Stretch)
        self.resultTable.verticalHeader().setSectionResizeMode(QtWidgets.QHeaderView.Stretch)

        self.importFile.clicked.connect(self.getFile)

        self.ResultPage = self.findChild(QtWidgets.QWidget, 'resultsPage')
        self.tabWidget.removeTab(2)

        self.statusBar.showMessage('Ready')

        self.show()

    def getFile(self):
        file = QtWidgets.QFileDialog.getOpenFileName(self, 'Open File', '', 'Java Files (*.java)')

        if file[0]:
            parser = classparser.ClassParser(file[0])
            self.start_worker(parser)
        
    def start_worker(self, parser):
        self.worker = Worker(self.models, parser)
        self.worker.update_progress.connect(self.update_progress)
        self.worker.result_ready.connect(self.update_table)
        self.worker.start()

    def update_progress(self, value, total):
        self.statusBar.showMessage(f'Processing {value} of {total}')

    def update_table(self, smells):
        self.tabWidget.addTab(self.ResultPage, 'Results')
        for i in range(5):
            self.resultTable.setItem(i, 1, QtWidgets.QTableWidgetItem(f'{smells[i]}'))
        self.tabWidget.setCurrentIndex(2)
        self.statusBar.showMessage('Done')


if __name__ == '__main__':
    app = QtWidgets.QApplication(sys.argv)
    window = Ui()
    app.exec_()