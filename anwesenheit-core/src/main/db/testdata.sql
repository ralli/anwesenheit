-- alle Benutzer haben das Passwort: "test"
insert into benutzer(benutzer_id, benutzertyp, vorname, nachname, email, salt, password_hash) values ('ralli', 'native', 'Ralph', 'Juhnke', 'ralph.juhnke@f-i-solutions-plus.de', 'gglXRkkD/f38+lJ9obgoJpyzKy8=', 'RmXqGHbIT9t69LEXfxeJtVPDog7N4B9OcDstKexEmFs=');
insert into benutzer(benutzer_id, benutzertyp, vorname, nachname, email, salt, password_hash) values ('demo', 'native', 'Demo', 'Demoname', 'demo.demoname@f-i-solutions-plus.de', 'Xr8MYsK2KWMNM6U3Et0NNJihRgA=', '6Tv1wgaPJHX29UQrUnGopQHbJu2qqw+mGgsCiBXxjDM=');
insert into benutzer(benutzer_id, benutzertyp, vorname, nachname, email, salt, password_hash) values ('chef', 'native', 'Chef', 'Chefname', 'chef.chefname@f-i-solutions-plus.de', 'Lmkn7o7I3TILEDA+k9RgNFN4HLE=', 'i+GIQ9JhlVdAOAvWQmN2sfSszQ6H2Z3OKVilw/7zjLw=');
insert into benutzer(benutzer_id, benutzertyp, vorname, nachname, email, salt, password_hash) values ('boss', 'native', 'Boss', 'Bossname', 'boss.bossname@f-i-solutions-plus.de', '51ydpEDbJi7Q0NzDKFT5GyjoeEo=', 'GiOHFJWP2AJfWKHgfS6Qjw5L+WRbBtExb6rzJwggIA4=');
insert into benutzer_rolle values ('ralli', 'ADMIN');
insert into benutzer_rolle values ('demo', 'ERFASSER');

insert into antrag(id, benutzer_id, antrag_art, antrag_status, von, bis) values (1, 'ralli', 'URLAUB', 'NEU', '2013-11-13', '2013-11-20');
insert into bewilligung (id, antrag_id, position, bewilligungs_status, benutzer_id) values(1, 1, 1, 'BEWILLIGT', 'chef');
insert into bewilligung (id, antrag_id, position, bewilligungs_status, benutzer_id) values(2, 1, 2, 'OFFEN', 'boss');