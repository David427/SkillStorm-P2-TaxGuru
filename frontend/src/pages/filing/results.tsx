import {
  Form,
  Label,
  Button,
  Fieldset,
  TextInput,
  GridContainer,
  StepIndicator,
  StepIndicatorStep,
} from "@trussworks/react-uswds";
import { FormEvent } from "react";

import { useTranslation } from "react-i18next";

export default function Results() {
  const { t } = useTranslation();

  const handlePersonalInfo = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    return;
  };

  return (
    <main className="full-page">
      <GridContainer className="usa-section">
        <StepIndicator headingLevel="h3" ofText="of" stepText="Step">
          <StepIndicatorStep label={t("personal.title")} status="complete" />
          <StepIndicatorStep label={t("filing-info.title")} status="complete" />
          <StepIndicatorStep label={t("w2.title")} status="complete" />
          <StepIndicatorStep label={t("1099.title")} status="complete" />
          <StepIndicatorStep label={t("deductions.title")} status="complete" />
          <StepIndicatorStep label={t("review.title")} status="complete" />
          <StepIndicatorStep label={t("results.title")} status="current" />
        </StepIndicator>

        <Form onSubmit={handlePersonalInfo}>
          <Fieldset legend="Get started with an account.">
            <p>
              <abbr title="required" className="usa-hint usa-hint--required">
                *
              </abbr>{" "}
              indicates a required field
            </p>

            <Label htmlFor="email" requiredMarker>
              Email Address
            </Label>
            <TextInput id="email" name="email" type="email" required />

            <Label htmlFor="password">Create Password</Label>
            <TextInput id="password" name="password" type="password" />

            <Button type="submit">Create Account</Button>
          </Fieldset>
        </Form>
      </GridContainer>
    </main>
  );
}
