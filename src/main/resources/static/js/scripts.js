$( document ).ready(function() {
    getTable();
    getAllRoles();

    //NEW FORM AND POST
    $("#saveNewUser").click(function () {
        let roles = [];
        $("#newUserRole option:selected").each(function () {
            roles.push({
                id: $(this).attr("role-id"),
                type: $(this).html()} );
        });

        let user = {
            'username': $("#newUserName").val(),
            'password': $("#newUserPass").val(),
            'money': $("#newUserMoney").val(),
            'roles': roles
        };

        $.ajax({
            type: "POST",
            url: "/api/users",
            contentType: 'application/json;',
            data: JSON.stringify(user),
            success: function () {
                getTable();
            }
        });
    })

    //UPDATE FORM AND PUT
    $("#updateFormUser").click(function () {
        let roles = [];
        $("#updateUserRole option:selected").each(function () {
            roles.push({
                id: $(this).attr("role-id"),
                type: $(this).html()} );
        });

        let user = {
            'id': $("#updateUserId").val(),
            'username': $("#updateUserName").val(),
            'password': $("#updateUserPass").val(),
            'money': $("#updateUserMoney").val(),
            'roles': roles
        };

        $.ajax({
            type: "PUT",
            url: "/api/users",
            contentType: 'application/json;',
            data: JSON.stringify(user),
            success: function () {
                getTable();
            }
        });
    })
});

// GET ALL
function getTable() {
    $.ajax({
        type: 'GET',
        url: "api/users/",
        contentType: 'application/json;',
        dataType: 'JSON',
        success: function (listUsers) {
            $("#userTable").empty();

            for (let i in listUsers) {
                $("#userTable").append(
                    "<tr> \
                        <td>" + listUsers[i].id + "</td> \
                            <td>" + listUsers[i].username + "</td> \
                            <td>" + listUsers[i].password + "</td> \
                            <td>" + listUsers[i].money + "</td>> \
                            <td>" + getUserRoles(listUsers[i].roles) + "</td> \
                            <td>\
                                <button onclick='openEdit(" + listUsers[i].id + ")' type='button' class='btn-sm btn-primary' data-toggle='modal' data-target='#editUser'>Edit</button>"+
                    "<button onclick='deleteUser(" + listUsers[i].id +")' type='button' class='btn-sm btn-danger'>Delete</button></td> \
                        </tr>")
            }
        }
    })
}

function getUserRoles(roles) {
    let userRoleTypes = [];
    for (let i in roles) {
        userRoleTypes[i] = roles[i].type;
    }
    return userRoleTypes;
}

function getAllRoles() {
    $.ajax({
        type: "GET",
        url: "/api/roles",
        contentType: "application/json",
        dataType: "JSON",
        success: function (allRoles) {
            $("#updateUserRole, #newUserRole").attr("size", allRoles.length);
            for (let i in allRoles) {
                $("#updateUserRole, #newUserRole").append(
                    "<option role-id=" + allRoles[i].id + ">" + allRoles[i].type + "</option>"
                );
            }
        }
    });
}

//GET
function openEdit(id) {
    //GETTING THIS USER
    $.ajax({
        type: 'GET',
        url: "api/users/"+ id,
        contentType: 'application/json;',
        dataType: 'JSON',
        success: function (data) {
            let user = JSON.parse(JSON.stringify(data));
            let userRoles = getUserRoles(user.roles);
            //POPULATING FORM
            // $("#editTitle").append(user.username);
            $("#updateUserId").val(user.id);
            $("#updateUserName").val(user.username);
            $("#updateUserPass").val(user.password);
            $("#updateUserMoney").val(user.money);
            $("#updateUserRole option").each(function () {
                $(this).prop('selected', false);
                for (var i in userRoles) {
                    if ($(this).text() === userRoles[i]) {
                        $(this).prop('selected', true);
                    } else {
                        $(this).click('selected', false);
                    }
                }
            });
        }
    })
}

//DELETE
function deleteUser(id) {
    $.ajax({
        type: 'DELETE',
        url: "/api/users/" + id,
        contentType: "application/json",
        success: function () { //TODO data?
            getTable();
        },
        error: function (data) {
            console.log(data)
        }
    });
}
