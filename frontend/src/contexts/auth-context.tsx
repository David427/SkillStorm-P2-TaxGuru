import {
  Dispatch,
  useState,
  useEffect,
  useContext,
  createContext,
  SetStateAction,
} from "react";

import Cookies from "js-cookie";

interface Authorization {
  jwt: string | null;
  setJwt: Dispatch<SetStateAction<string | null>>;
  logout: () => void;
}

const AuthContext = createContext<Authorization>({
  jwt: null,
  setJwt: () => {},
  logout: () => {},
});

export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
  const [jwt, setJwt] = useState<string | null>(null);

  // we're gonna check if the user has a cookie saved for the jwt
  // and set it to the state if it is
  useEffect(() => {
    if (typeof window !== "undefined") {
      // check if the cookie exists
      const cookieJwt = Cookies.get("jwt");

      if (cookieJwt) setJwt(cookieJwt);
    }
  }, []);

  // when the JWT is updated, store it as a cookie
  useEffect(() => {
    if (!jwt) return;

    // update cookie
    Cookies.set("jwt", jwt, {
      sameSite: "strict",
      expires: 1, // 1 day from now
    });
  }, [jwt]);

  const logout = () => {
    setJwt(null);
    Cookies.remove("jwt");
  };

  return (
    <AuthContext.Provider value={{ jwt, setJwt, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

// eslint-disable-next-line react-refresh/only-export-components
export const useAuth = () => {
  return useContext(AuthContext);
};
