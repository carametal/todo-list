INSERT INTO public.users
(username, "password", enabled)
values
('admin', 'admin', true),
('user', 'user', true);

INSERT INTO public.authorities
(user_id, authority)
values
(1, 'ADMIN'),
(2, 'USER');
