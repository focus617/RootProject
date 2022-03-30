import com.example.backend.utils.CommandLineInput

//package com.example.backend.utils
//
//import com.example.pomodoro2.data.AppInMemoryDataSource
//import com.example.pomodoro2.domain.model.Task
//import com.example.pomodoro2.domain.repository.DefaultTaskRepository
//
class CommandLineInputHandler {
//
//    private var toDoRepository = DefaultTaskRepository.getInstance(
//        FakeDataSource(),
//        FakeDataSource(),
//        AppInMemoryDataSource()
//    )

    fun printOptions() {
        println("\n--- To Do Application ---")
        println("Please make a choice:")
        println("(a)ll tasks")
        println("(f)ind a specific task")
        println("(i)nsert a new task")
        println("(u)pdate an existing task")
        println("(d)elete an existing task")
        println("(e)xit")
    }

    fun readInput(): String {
        print("> ")
        return readLine() ?: "e"
    }

    fun processInput(input: CommandLineInput?) {
        if (input == null) {
            handleUnknownInput()
        } else {
            when (input) {
//                CommandLineInput.FIND_ALL -> printAllToDoTasks()
//                CommandLineInput.FIND_BY_ID -> printToDoTask()
//                CommandLineInput.INSERT -> insertToDoTask()
//                CommandLineInput.UPDATE -> updateToDoTask()
//                CommandLineInput.DELETE -> deleteToDoTask()
                CommandLineInput.EXIT -> {}
                else -> {}
            }
        }
    }
//
//    private fun askForTaskId(): String {
//        println("Please enter the task ID:")
//        return readInput()
//    }
//
//    private fun askForNewToDoTask(): Task {
//        val toDoTask = Task.create()
//        println("Please enter the name of the task:")
//        toDoTask.title = readInput()
//        return toDoTask
//    }
//
//    private fun printAllToDoTasks() {
//        val toDoTasks: List<Task> = toDoRepository.selectBy(null)
//        if (toDoTasks.isEmpty()) {
//            println("Nothing to do. Go relax!")
//        } else {
//            for (toDoTask in toDoTasks) {
//                println(toDoTask)
//            }
//        }
//    }
//
//    private fun printToDoTask() {
//        val toDoTask: Task? = findTask()
//        if (toDoTask != null) {
//            println(toDoTask)
//        }
//    }
//
//    private fun findTask(): Task? {
//        val id = askForTaskId()
//        val toDoTask = toDoRepository.selectBy(OfIdTaskSpec(id))
//        if (toDoTask.isEmpty()) {
//            System.err.println("To do task with ID $id could not be found.")
//        }
//        return toDoTask.getOrNull(0)
//    }
//
//    private fun insertToDoTask() {
//        val toDoTask: Task = askForNewToDoTask()
//        val id: String = toDoRepository.insert(toDoTask)
//        println("Successfully inserted to do task with ID $id.")
//    }
//
//    private fun updateToDoTask() {
//        val toDoTask: Task? = findTask()
//        if (toDoTask != null) {
//            println(toDoTask)
//            println("Please enter the name of the task:")
//            toDoTask.title = readInput()
//            println("Please enter the done status the task:")
//            when (readInput().toBoolean()) {
//                true -> toDoTask.complete()
//                false -> toDoTask.activate()
//            }
//            toDoRepository.insert(toDoTask)
//            println("Successfully updated to do task with ID ${toDoTask.id}.")
//        }
//    }
//
//    private fun deleteToDoTask() {
//        val toDoTask: Task? = findTask()
//        if (toDoTask != null) {
//            toDoRepository.delete(toDoTask)
//            println("Successfully deleted to do task with ID  ${toDoTask.id}.")
//        }
//    }

    private fun handleUnknownInput() {
        println("Please select a valid option!")
    }
}