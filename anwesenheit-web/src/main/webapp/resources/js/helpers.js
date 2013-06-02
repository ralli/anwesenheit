var app = angular.module("app.helpers", []);

app.factory("helpers", function () {
  return {
    rowClassForAntrag: function (antrag) {
      var status = antrag.antragStatus.antragStatus,
          result = "";

      if (status === 'NEU') {
        result = '';
      } else if (status === 'IN_ARBEIT') {
        result = 'info';
      } else if (status === 'BEWILLIGT') {
        result = 'success';
      } else if (status === 'ABGELEHNT') {
        result = 'error';
      } else if (status === 'STORNIERT') {
        result = 'storniert';
      }

      return result;
    },

    rowClassForBewilligung: function (b) {
      var status = b.bewilligungsStatus.bewilligungsStatus,
          result = "";

      if (status === 'ABGELEHNT') {
        result = "error";
      } else if (status === 'OFFEN') {
        result = "";
      } else if (status === 'BEWILLIGT') {
        result = "success";
      }

      return result;
    },

    rolleForBewilliger: function (b) {
      if (b.position === 1) {
        return "1. Unterschrift";
      } else if (b.position === 2) {
        return "2. Unterschrift";
      }
      else {
        return "Zur Info";
      }
    }
  };
});