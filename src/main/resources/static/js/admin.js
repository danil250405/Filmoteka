function setUserIdInModal(userId) {
    document.querySelector('.userId').textContent = userId;
}


    $('#banUserModal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget); // Button that triggered the modal

    // Get user data from data-* attributes
    var userId = button.data('user-id');
    var email = button.data('user-email');
    var username = button.data('user-username');
    var role = button.data('user-role');

    // Update the modal content with user data
    var modal = $(this);
    modal.find('#userId').val(userId);
    modal.find('#email').text(email);
    modal.find('#username').text(username);
    modal.find('#role').text(role);
});
    $('#unbanUserModal').on('show.bs.modal', function (event) {
    // const banReasonsUnb = document.querySelector('#forUnbanBanReasons')
    var button = $(event.relatedTarget); // Button that triggered the modal
    var userId = button.data('user-id'); // Get user ID from data-* attribute
    var email = button.data('user-email');
    var username = button.data('user-username');
    var role = button.data('user-role');
    // var sss = button.
    //
    // \
    // var banTimes = button.data('user-banDateTime');
    var banReasonsUnb = button.data('user-banreasonunb');
        var banTimes = button.data('user-bandatetime');

        // var banReasonsUnb = $('#forUnbanBanReasons').val();
    // var banTimes = $('#banTimes').val();

    // Set the value of the hidden input field
    console.log(userId, email, username, role, banReasonsUnb)
    var modal = $(this);
    modal.find('#unban-userId').val(userId);
    modal.find('#unban-email').text(email);
    modal.find('#unban-username').text(username);
    modal.find('#unban-role').text(role);
    modal.find('#unban-banreasonsunb').text(banReasonsUnb);
    modal.find('#unban-bantimes').text(banTimes);
    // Update other modal content as needed
});
