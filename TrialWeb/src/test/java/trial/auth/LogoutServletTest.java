package trial.auth;

import static org.mockito.Mockito.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LogoutServletTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testDoGet_session() throws ServletException, IOException {
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);

		HttpSession session = mock(HttpSession.class);
		when(request.getSession(false)).thenReturn(session);

		when(request.getContextPath()).thenReturn("/TrialWeb");

		new LogoutServlet().doGet(request, response);

		/* Redirectが呼ばれていることを確認 */
		verify(response, times(1)).sendRedirect("/TrialWeb/auth/basic/");
	}


	@Test
	void testDoGet_sessionNull() throws ServletException, IOException {
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);

		when(request.getSession(false)).thenReturn(null);

		when(request.getContextPath()).thenReturn("/TrialWeb");

		new LogoutServlet().doGet(request, response);

		/* Redirectが呼ばれていることを確認 */
		verify(response, times(1)).sendRedirect("/TrialWeb/auth/basic/");
	}
}
