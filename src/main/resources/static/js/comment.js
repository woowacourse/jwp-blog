const TODOAPP = (function () {
    'use strict';

    const todoTemplate =
        "<li>"+
            "<div class=\"view\">"+
                "<input class=\"toggle\" type=\"checkbox\">"+
                "<label class='label'>{{todoTitle}}</label>"+
                "<button class=\"destroy\"></button>"+
            "</div>"+
            "<input class=\"edit\" value=\"{{todoTitle}}\">"+
        "</li>";

    const todoItemTemplate = Handlebars.compile(todoTemplate);

    //todo 컨트롤러에 각 이벤트에 해당하는 이벤트 리스너를 등록해야한다.
    const TodoController = function () {
        const todoService = new TodoService()

        const addTodo = function () {
            const todoTitle = document.getElementById('new-todo-title')
            todoTitle.addEventListener('keydown', todoService.add)
        }

        //todo 리스트 앞의 체크박스를 클릭했을 떄 완료된 표시를 해줘야 한다.
        const completeTodo = function () {
        };

        //todo 리스트에서 x버튼을 눌렀을 때 삭제 할 수 있어야 한다.
        const deleteTodo = function () {
        };

        //todo 리스트의 타이틀을 변경할 수 있어야 한다.
        const updateTodo = function () {

        }

        const init = function () {
            addTodo()
            completeTodo()
            deleteTodo()
            updateTodo()
        }

        return {
            init: init
        }
    }


    const TodoService = function () {
        const add = function (event) {
        }

        return {
            add:add
        }
    }

    const init = function () {
        const todoController = new TodoController()
        todoController.init()
    };

    return {
        init: init,
    };
})();

TODOAPP.init();
