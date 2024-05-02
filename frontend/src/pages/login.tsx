import {
  Grid,
  Form,
  Label,
  Button,
  Fieldset,
  TextInput,
  GridContainer,
} from "@trussworks/react-uswds";
import { Link } from "react-router-dom";
import { FormEvent, useState } from "react";
import { useTranslation } from "react-i18next";

export default function Login() {
  const { t } = useTranslation();
  const [showPassword, setShowPassword] = useState(false);

  const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const formData = {
      // @ts-expect-error untyped form elements but we need the values
      username: e.currentTarget.elements.username.value,
      // @ts-expect-error untyped form elements but we need the values
      password: e.currentTarget.elements.password.value,
    };

    console.log(formData);
    e.currentTarget.reset();
  };

  return (
    <main className="full-page">
      <div className="bg-base-lightest">
        <GridContainer className="usa-section">
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
                      required
                    />

                    <Label htmlFor="password">{t("auth.password")}</Label>
                    <TextInput
                      id="password"
                      name="password"
                      type={showPassword ? "text" : "password"}
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
