$(document)
    .ready(
        function() {
          $('.delete-link')
              .click(
                  function(event) {
                    event.preventDefault();
                    if (confirm("Möchten Sie diesen Eintrag wirklich löschen?")) {
                      $(
                          '<form method="POST" style="display:none"><input type="hidden" name="_method" value="DELETE" /></form>')
                          .insertAfter($(this)).attr({
                            action : $(this).attr('href')
                          }).submit();
                    }
                    return false;
                  });
        });
