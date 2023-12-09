
INSERT INTO public.users
(username, "password", enabled)
values
('admin', crypt('admin', gen_salt('bf', 10)), true),
('user', crypt('admin', gen_salt('bf', 10)), true);

INSERT INTO public.authorities
(user_id, authority)
values
(1, 'ADMIN'),
(2, 'USER');
