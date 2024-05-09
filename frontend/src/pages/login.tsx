import type { AuthResponse } from "../types";

import {
  Grid,
  Form,
  Alert,
  Label,
  Button,
  Fieldset,
  TextInput,
  GridContainer,
} from "@trussworks/react-uswds";
import { useTranslation } from "react-i18next";
import { useAuth } from "../contexts/auth-context";
import { FormEvent, useState } from "react";
import { Link, useNavigate, Navigate } from "react-router-dom";

export default function Login() {
  const navigate = useNavigate();

  const { t } = useTranslation();
  const { jwt, setJwt, setUsername } = useAuth();

  const [error, setError] = useState<string | null>(null);
  const [showPassword, setShowPassword] = useState(false);

  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError(null);

    const formData = {
      // @ts-expect-error untyped form elements but we need the values
      username: e.currentTarget.elements.username.value,
      // @ts-expect-error untyped form elements but we need the values
      password: e.currentTarget.elements.password.value,
    };

    const res = await fetch("http://localhost:8080/auth/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(formData),
    });

    if (res.ok) {
      const data: AuthResponse = await res.json();
      if (data.jwt) setJwt(data.jwt);
      setUsername(formData.username);
      navigate("/");
    } else {
      const text = await res.text();

      setError(text);
    }
  };

  // redirect them to the account page if they are already signed in.
  if (jwt) {
    return <Navigate to="/account" />;
  }

  return (
    <main className="full-page">
      <div className="bg-base-lightest">
        <GridContainer className="usa-section">
          {/* Error Alert */}
          <Grid row className="flex-justify-center margin-bottom-205">
            <Grid col={12} tablet={{ col: 8 }} desktop={{ col: 6 }}>
              {error && (
                <Alert
                  type="error"
                  heading="Error Logging In"
                  headingLevel="h4"
                >
                  {error}
                </Alert>
              )}
            </Grid>
          </Grid>

          <Grid row className="flex-justify-center">
            <Grid col={12} tablet={{ col: 8 }} desktop={{ col: 6 }}>
              <div className="bg-white padding-y-3 padding-x-5 border border-base-lightest">
                <h1 className="margin-bottom-0">{t("auth.login")}</h1>
                <Form onSubmit={handleSubmit}>
                  <Fieldset legend={t("auth.login-desc")} legendStyle="default">
                    <Label htmlFor="username">{t("auth.username")}</Label>
                    <TextInput
                      id="username"
                      name="username"
                      type="text"
                      autoComplete="username"
                      required
                    />

                    <Label htmlFor="password">{t("auth.password")}</Label>
                    <TextInput
                      id="password"
                      name="password"
                      type={showPassword ? "text" : "password"}
                      autoComplete="current-password"
                    />

                    <button
                      title="Toggle Password Visibility"
                      type="button"
                      className="usa-show-password"
                      aria-controls="password password_confirm"
                      onClick={() => setShowPassword((prev) => !prev)}
                    >
                      {showPassword
                        ? t("auth.hide-password")
                        : t("auth.show-password")}
                    </button>

                    <Button type="submit">{t("auth.login")}</Button>
                  </Fieldset>
                </Form>
              </div>

              <p className="text-center">
                {t("auth.no-account")}{" "}
                <Link to="/signup" className="usa-link">
                  {t("auth.create-account1")}
                </Link>
              </p>
            </Grid>
          </Grid>
        </GridContainer>
      </div>
    </main>
  );
}
